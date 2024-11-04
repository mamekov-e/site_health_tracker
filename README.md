Configurations:

1. Create bot in telegram, add username and token
2. Webhook path
   - make telegram endpoint public if it's close by authenticator
   - install and setup ngrok.exe from https://ngrok.com/download (with docker, extra proxy settings required)
   - ngrok.exe is executable file with yuo need to use to run ngrok commands
     - go to 'Getting started' -> 'Setup & Installation' tab and choose your agent (https://dashboard.ngrok.com/get-started/setup/windows)
     - run ./ngrok.exe config add-authtoken {your_ngrok_token}
       - ngrok_token can be generated in 'Your Authtoken tab' sidebar
     - choose static domain tab 
     - run ./ngrok.exe http --url={your_static_domain} {your_service_port}
       - static_domain can be generated in 'Cloud Edge' -> 'Domains' sidebar
     - run your service with some port
       - in forwarding copy webhook path and ngrok token to use it in your service properties
     - copy https://api.telegram.org/bot{token}/setWebhook?url={webhookpath} with set bot token and webhook path and paste to browser to set webhook
     - go to webhook path from forwarding (it's done you)
3. in order to use gmail mailing create account and turn on two-factor authentication
4. create password app and set in application.properties username and password 