[![CI](https://github.com/cardouken/danker-giphy-slack-bot/actions/workflows/ci.yml/badge.svg)](https://github.com/cardouken/danker-giphy-slack-bot/actions/workflows/ci.yml)

# danker-giphy-slack-bot
Like Giphy for Slack, but danker

Emulates /giphy Slack integration behaviour, but depending on configuration will randomly replace some gifs only after the user clicks "Send" in the ephemeral message. As long as the user hasn't pressed "Send", shuffling works just like the actual /giphy integration and will display random gifs to choose from.

The following is configurable:

* Chance threshold from 0-100% that a gif will be replaced upon sending
* Channels which to monitor
* Target keywords which to monitor when `/giphy keyword` is triggered
* Replacement keywords which to replace the gif with upon pressing send


# Usage
* `/giphy some keyword` to find a random gif and shuffle through them to send or cancel

Since Slack's bot API requires explicit permissions or has to be part of direct messages to trigger bot commands, then this will only work in channels (private and public). 

The real Giphy integration will also post as the user whereas this bot will post as an app named `giphy`. This means users are unable to delete the posted gifs as they usually would. As a replacement, you can do the following instead:

* Post a slash command with the message's permalink you want to delete: `/giphy https://your-workspace.slack.com/archives/C02AXH70FGA/p163131522603560`
* Configure an emoji and if the emoji is used to react to the post, the bot will delete its post. The default emoji which deletes messages in this implementation is `:monkas:` for when it gets a bit too real.

# Demo
![Demo](/src/main/resources/giphy-demo.gif)

