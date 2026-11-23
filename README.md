# Verity AI — Forge 1.16.5

An original companion-and-horror mod by FYreDeft. This project uses broad genre inspiration only; it does not copy another project's source code, dialogue, textures, models, or branding. It is not affiliated with ThatMob or the Verity JE team.

## Target
- Minecraft Java Edition 1.16.5
- Minecraft Forge 36.2.39
- Java 8
- Designed to avoid OptiFine-sensitive coremod and renderer patches. Actual OptiFine compatibility still needs in-game verification.

## Current source features
- Registered custom companion entity with a simple bobbing model.
- Operator command /verity summon.
- /verity ask <message> sends a Gemini request asynchronously.
- /verity status.
- Private local Forge config for API key and model.
- API cooldown and network timeouts.
- Player-specific sleep counter and in-game-only remembered sleep coordinates.
- Friendly companion wandering and following behaviour.

## Planned expansion
Animated arrival crate, richer mood/personality, persistent chat memory, house/farm building, resource-aware requests, player-specific gift inventory, favourite-place journal, biome journal, adventure recaps, three-night progression, distant watcher, copied structures, ambience, configurable horror intensity, accessibility controls, multiplayer isolation, and a status screen. These are roadmap items until implemented and tested.

## Gemini setup
1. Run once to generate config/verityai-common.toml.
2. Close Minecraft and set apiKey locally.
3. Keep the key private. Never commit the config or embed the key in a shared JAR.
4. Restart and use /verity ask hello.

Default model: `gemini-2.5-flash`. You can change the model ID in the local config file if your AI Studio key has access to another Gemini model. Model access and quotas depend on the Google AI Studio account and can change.

## Build
Use JDK 8 and Gradle 6.9.4, then run gradle build. The JAR is written to build/libs/.

This repository is not a finished release until CI passes and the mod is tested in a real 1.16.5 client and server.
