package de.rettichlp.shantybot.common.services;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.allOf;

@EnableScheduling
@Log4j2
@Component
public class RoleSyncService {

    @Scheduled(cron = "0 * * * * ?", zone = "Europe/Berlin") // every day at 0:00, 6:00, 12:00, 18:00 (UTC+1)
    public void deleteOldLogEntries() {
        long startTime = currentTimeMillis();
        log.info("Discord role synchronising: started");

        ofNullable(discordBotProperties.getGuild()).ifPresentOrElse(guild -> {
            Role memberRole = discordBotProperties.getMemberRole();
            List<CompletableFuture<Void>> futures = guild.loadMembers().get().stream()
                    .filter(member -> nonNull(memberRole))
                    .filter(member -> !member.getUser().isBot())
                    .filter(member -> !member.getRoles().contains(memberRole))
                    .map(member -> {
                        CompletableFuture<Void> future = new CompletableFuture<>();
                        guild.addRoleToMember(member, memberRole).queue(success -> {
                            log.info("Discord role synchronising: Add role {} to member {}", memberRole.getName(), member.getEffectiveName());
                            future.complete(null);
                            }, future::completeExceptionally);
                        return future;
                    })
                    .toList();

            allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> log.info("Discord role synchronising: finished in {}ms", currentTimeMillis() - startTime)).exceptionally(error -> {
                log.error("Discord role synchronising: failed - {}", error.getMessage());
                return null;
            });
        }, () -> log.warn("Discord role synchronising: Skipped! Guild is null"));
    }
}
