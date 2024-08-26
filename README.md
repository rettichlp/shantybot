# [ShantyBot](https://discord.com/oauth2/authorize?client_id=725076053495644201)

[![Release](https://github.com/rettichlp/shantybot/actions/workflows/release.yml/badge.svg)](https://github.com/rettichlp/shantybot/actions/workflows/release.yml)
[![Build](https://github.com/rettichlp/shantybot/actions/workflows/build.yml/badge.svg)](https://github.com/rettichlp/shantybot/actions/workflows/build.yml)

This bot was created for the Minecraft Server ShantyTown (shantytown.eu) to provide some useful features for the community.
Features are focused on the Minecraft server, so this bot won't work properly on other servers.
For this reason the sourcecode is public, so you can steal the code and modify it for your own server ;) .

<!-- TOC -->
* [ShantyBot](#shantybot)
  * [Permissions](#permissions)
    * [General Permissions](#general-permissions)
    * [Activities regarding permissions](#activities-regarding-permissions)
  * [Commands](#commands)
<!-- TOC -->

## Permissions

The bot needs the following permissions to work properly:

### General Permissions

The bot will ask for these permissions:

| Permission           | Usage                                                                 |
|----------------------|-----------------------------------------------------------------------|
| Add Reactions        | Currently no usages - potential for upcoming features                 |
| Embed Links          | Required to send embedded messages                                    |
| Manage Channels      | Required to change the topic of the Welcome-Channel                   |
| Manage Messages      | Required to delete messages to keep the Discord server clean and safe |
| Manage Nicknames     | Currently no usages - potential for upcoming features                 |
| Manage Roles         | Required to add the Member-Role to new members                        |
| Read Message History | Required to retrieve messages to be deleted                           |
| Speak                | Required for the music bot                                            |
| Use Slash Commands   | Required for the music bot and other features                         |

### Activities regarding permissions

With these permissions the bot has access to following activities:

![](https://private-user-images.githubusercontent.com/97811064/355839024-aa4e9f31-4852-4d71-b57c-fc3d3a416472.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjMwMzgxNTMsIm5iZiI6MTcyMzAzNzg1MywicGF0aCI6Ii85NzgxMTA2NC8zNTU4MzkwMjQtYWE0ZTlmMzEtNDg1Mi00ZDcxLWI1N2MtZmMzZDNhNDE2NDcyLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA4MDclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwODA3VDEzMzczM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWUwMmFiMTU5ODdlOThhYTFlY2U3YjgzOGM1ZTJkZWQxMWMxNDZiNjk0YjBiNDIzYjgyNzNiNDA4ZGMzZWNhOGMmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.QGpqYJL29ol7zK5hJ_FRYgvAkj1T7mwmYmmNUiGxFo4)

## Commands

The bot provides the following slash commands:

| Command                           | Description                                                                 |
|-----------------------------------|-----------------------------------------------------------------------------|
| `/löschen` `amount`               | Deletes the last \<amount> messages in the current channel                  |
| `/befehle`                        | Shows all available commands of the ShantyBot                               |
| `/ip`                             | Shows the IP, version and additional information about the Minecraft server |
| `/musik` `search query` or `link` | Searches for a song on YouTube or via link (YouTube, Soundcloud, ...)       |
| `/spieler`                        | Shows the number of players currently on the Minecraft server               |
| `/version`                        | Shows the current version of the bot                                        |
