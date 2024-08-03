package de.rettichlp.shantybot.common.services;

import de.rettichlp.shantybot.common.configuration.DiscordBotProperties;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.HOURS;

@Log4j2
@Component
public class RoleSyncService {

    public RoleSyncService(DiscordBotProperties discordBotProperties) {
        long sixHoursInMillis = HOURS.toMillis(6);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long startTime = currentTimeMillis();
                log.info("Discord role synchronising: started");

                Guild guild = discordBotProperties.getGuild();
                guild.loadMembers().get().forEach(member -> {
                    Role memberRole = discordBotProperties.getMemberRole();
                    if (!member.getRoles().contains(memberRole)) {
                        guild.addRoleToMember(member, memberRole).queue();
                        log.info("Discord role synchronising: Add role {} to member {}", memberRole, member.getEffectiveName());
                    }
                });

                log.info("Discord role synchronising: finished in {}ms", currentTimeMillis() - startTime);
            }
        }, (sixHoursInMillis - currentTimeMillis() % sixHoursInMillis) + HOURS.toMillis(2), sixHoursInMillis); // 4 10 16 22
        log.info("Discord role synchronising: scheduled");
    }
}
