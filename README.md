Configurations:

1. Create bot in telegram, add username and token
2. Webhook path
   - install and setup ngrok.exe from https://ngrok.com/download
   - open and paste ngrok http and serve port (ngrok http 8080)
   - in forwarding copy webhook path ('https://23b1a54ccbbd.ngrok.io'), it is given for 2 hours
   - copy https://api.telegram.org/bot{token}/setWebhook?url={webhookpath} and paste to browser to set webhook
   - in advance go to https://ngrok.com/upgrade, create domain and replace webhook with created one remove expiration
3. in order to use gmail mailing create account and turn on two-factor authentication
4. create password app and set in application.properties username and password 