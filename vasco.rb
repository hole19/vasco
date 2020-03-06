require 'webrick'
require 'terrapin'
require 'yaml'
require 'fileutils'

def open_file(file_name, method, should_exist)
  if !File.file?(File.expand_path(file_name))
    if should_exist
      puts "File #{file_name} not found!"
      return nil
    else
      FileUtils.mkdir_p(File.dirname(file_name))
    end
  end

  File.open(File.expand_path(file_name), method)
end

def launch_server
  options = { :Port => 8000, :DocumentRoot => File.expand_path('./public') }

  # Set: ERB Handler
  WEBrick::HTTPServlet::FileHandler.add_handler("erb", WEBrick::HTTPServlet::ERBHandler)

  server = WEBrick::HTTPServer.new(options)
  server.config[:MimeTypes]["erb"] = "text/html"

  yield server

  trap 'INT' do server.shutdown end
  server.start
end

def is_float(param)
  param.to_f.to_s == param.to_s
end

def is_location(params)
  is_float(params['lat']) &&
    is_float(params['lng']) &&
    params['lat'].to_f.between?(-90, 90) &&
    params['lng'].to_f.between?(-180, 180)
end

######### RUN #########

CONFIG = YAML.load(open_file("config.yml", "r", true))

puts CONFIG

launch_server do |server|
  server.mount_proc '/location' do |req, res|
    unless is_location(req.query)
      puts "INVALID LOCATION: (#{req.query['lat']},#{req.query['lng']})"
      res.status = 400
      res.body = "bad params! #{req.query}"
      exit
    end

    if dest_cmd = CONFIG['destinations'][req.query['destination']]
      cmd = Terrapin::CommandLine.new('', dest_cmd)
      params = { lat: req.query['lat'].to_f, lng: req.query['lng'].to_f }
      puts "> #{res.body = cmd.command(params)}"
      cmd.run(params)

    else
      puts "INVALID DESTINATION: '#{req.query['destination']}'"
      res.status = 400
      res.body = "bad params! #{req.query}"
    end
  end
end
