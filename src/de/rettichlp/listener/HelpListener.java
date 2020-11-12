package de.rettichlp.listener;

import java.util.Random;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentDisplay().toLowerCase();
		
		if(event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
			TextChannel channel = event.getTextChannel();
			
			String[] witz = new String[] {
					"Ich hab mit meinen Pflanzen ein Abkommen getroffen: ab jetzt nur noch alle zwei Wochen Wasser. Sie sind drauf eingegangen.",
					"Meine Kumpels und ich werden uns nachher Ferngläser kaufen. - Und dann? - Dann sehen wir weiter.",
					"Wie nennt man ein verrücktes Weizenprodukt? – Gagamehl!",
					"Was erhält man, wenn man 1 durch Z teilt? – Ein Zettel.",
					"Ruft ein Mann ein Sandwich an, aber es war belegt.",
					"Wo geht Abnehmen ohne zu hungern? – Am Telefon!",
					"Fragt die eine Kerze die andere: Sag mal ist Wasser eigentlich gefährlich? Darauf die Andere: Davon gehe ich aus!",
					"Zwei Ameisen tragen ein Fenster durch die Wüste. Da sagt die eine: Mir ist sooo heiß! Sagt die andere: Dann mach doch das Fenster auf!",
					"Welche Spinne kann fliegen? – Die Vogelspinne.",
					"Hast du schon mal ein Kühlschrank in freier Laufbahn gesehen? – Nein? Kannst mal sehen wie schnell die sind!",
					"Was trinken Firmenchefs? – Leitungswasser.",
					"Wollte gerade einen Witz über die deutsche Bahn machen. Weiß aber nicht ob er angekommen ist?!",
					"Unglaublich! Mein Nachbar hat doch tatsächlich mitten in der Nacht um 2 Uhr bei mir geklingelt! Mir wäre fast die Schlagbohrmaschine aus der Hand gefallen!",
					"Ich habe meine Ernährung umgestellt! Die Kekse stehen jetzt links vom Laptop.",
					"Gute Nachricht: Ich bekomme endlich den obersten Knopf meiner superengen Jeans zu. Schlechte Nachricht: Habe sie leider nicht an.",
					"Kauf dir einen Bumerang, für einen Frisbee braucht man Freunde! Herzlich Willkommen im Diss-cord",
					"Mann, ich hab es satt, herumzuhängen!, sagte die Glühbirne und brannte durch.",
					"Immer wenn du am Boden liegst und denkst: Super! Tiefer geht’s nicht!, kommt jemand und wirft dir ’ne Schaufel zu.",
					"Werbepause ARD: Kurz mal popeln.\n Werbepause RTL: Eine rauchen.\n Werbepause SAT1/Pro7: Einmal McDrive und kurz zur Tanke.",
					"Da wollte ich der netten, alten Dame über die Straße helfen, als ich merkte, dass es eine 18-jährige auf dem Weg ins Solarium war."
			};

			//Freunde hinzufügen in verschiedenen Welten
			if (message.contains("trust") || message.contains("add") || message.contains("hinzufügen") || message.contains("trust") || message.contains("wie füg")) {
				channel.sendMessage("World: /as addfriend <Name>\nSurvival: /trust <Name>\nCreative: /p add <Name>\nAcidIsland: /ai team invite <Name>").queue();				
			}
			//spenden
			if (message.contains("spenden")) {
				channel.sendMessage("Du willst spenden? Schreib **ScuroK** einfach eine **Mail** auf dem Server (/mail send ScuroK Ich möchte gern spenden.). Er wird sich dann bei dir melden. Am besten ist eine Spende mittels PaySafe-Card.").queue();
			} else if (message.contains(" spender")) {
				channel.sendMessage("Du willst wissen, was Spender sind? **Am Spawn** gibt es ein Portal, in dem du **alle Informationen** dazu findest!").queue();
			}
			//Bot Spaß
			if (message.contains("wie ") && message.contains("alt")) {
				channel.sendMessage("Ab einem bestimmten Alter sollte man nicht mehr darüber reden...").queue();
			} else if (message.contains("mega nice")) {
				channel.sendMessage("Ich weiß, ich bin immer der beste unter euch " + event.getGuild().getEmotesByName("scu", true).get(0).getAsMention()).queue();
			} else if (message.contains("witz")) {
				Random rand = new Random();
				int i = rand.nextInt(witz.length);
				channel.sendMessage("Ja ein Witz! \n" + witz[i]).queue();
			} else if (message.contains("fakt")) {
				channel.sendMessage("Du willst einen krassen Fakt hören? 'Dreh mal am Herd' ergibt rückwärts genau das selbe!").queue();
			} else if (message.contains("saufen")) {
				channel.sendMessage("Trink niemals zuviel, denn die nächste Flasche, die draufgeht, könntest du sein!").queue();
			}
			//Server
			if (message.contains("ip ") || message.contains("ip?")) {
				channel.sendMessage("Die Server-IP ist **shantytown.eu**.").queue();
			} else if (message.contains("owner")) {
				channel.sendMessage("Der Owner von shantytown.eu ist **ScuroK** *(" + event.getGuild().getOwner().getOnlineStatus() + ")*").queue();
			}
			//channel.sendMessage(event.getMessage().getMentionedMembers().get(0).getOnlineStatus().toString()).queue();
		}
	}
}