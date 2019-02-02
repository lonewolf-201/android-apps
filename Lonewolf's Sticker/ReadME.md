My first app.
Its a sticker app.
But to be honest i didn't do much of coding or any of the these stickers. I just took this source code from WhatsApp's github repo and 
added stickers that I downloaded from a website. Did some tweaking to the stickers on photoshop and edited the JSON.


IT WAS SO EASY.....

I will tell you how to in Layman's terms....



Step 1:-
        Download this repo.
        
Step 2:-
        Download your stickers and group them into folders.
        for example:- 
                  Winnie the pooh stickers in a folder called winnie the pooh folder.
Step 3:-
        make sure your stickers at size of 512 X 512 pixels or else you won't be able to add them.
        the format should be in png so that we could change it to webp format in android studio.
Step 4:-
        open android studio and choose open from existing project.
        browse for the downloaded source code. import it onto your android studio.
Step 5:-
        open your explorer and navigate to the source code folder.
        app>src>main>assests>create a folder for the sticker pack. ### although I suggest a simple name such as 1,2,3,4.
        put your ping stickers that are sized to 512 X 512 px.
        the tray image should in 96 X 96 px.
Step 6:-
        Go to your android studio update the JSON file in assests folder.
        do the editing as given below:-
     {
      "identifier": "*name of the coressponding folder in assest that has the sticker*",
      
      "name": "*name you would like to show in the whatsapp*",
      
      "publisher": "*publisher name*",
      
      "tray_image_file": "*the image to show in the whatsapp sticker app*",
      
      "publisher_email":"*ur email*",
      
      "publisher_website": "*ur website*",
      
      "privacy_policy_website": "",
      
      "license_agreement_website": "",
      
      "stickers": [
      
        {
        
          "image_file": "Panda_1.webp",
          
          "emojis": ["☕","🙂"]
          
        },
        
        {
        
          "image_file": "Panda_2.webp",
          
          "emojis": ["😄","😀"]
          
        },
        
        {
        
          "image_file": "Panda_3.webp",
          
          "emojis": ["😆","😂"]
          
        },
        
        {
        
          "image_file": "Panda_4.webp",
          
          "emojis": ["😩","😰"]
          
        },
        
        {
        
          "image_file": "Panda_5.webp",
          
          "emojis": ["😭","💧"]
          
        },
        
        {
        
          "image_file": "Panda_6.webp",
          
          "emojis": ["😍","♥"]
          
        },
        
        {
        
          "image_file": "Panda_7.webp",
          
          "emojis": ["💔","👎"]
          
        }
        
        compile and build the project and the app should be in 
        app>src>main>build>app-debug.apk
        install and have fun.
