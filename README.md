# Verity AI — Forge 1.16.5

An original companion-and-slow-burn-horror mod by FYreDeft. It uses broad genre inspiration only; it does not copy another project's source code, dialogue, textures, models, or branding. It is not affiliated with ThatMob or the Verity JE team.

## Target
- Minecraft Java Edition 1.16.5
- Minecraft Forge 36.2.42
- Java 8
- ForgeGradle 5.1.77 / Gradle 7.3.3
- No coremod or invasive renderer patches. OptiFine compatibility is a design goal and still requires an in-game test.

## Implemented in source
- Original layered brass automaton model with a custom pixel-art atlas, cyan faceplate, chest sigil, stabilizer wings, thrusters, antenna core, and segmented halo.
- Hover bob, gentle yaw drift, wing-beat, antenna motion, and a rotating halo animation.
- Dedicated mod-owned texture resource generated into the JAR at build time; no vanilla block texture dependency.
- Standard Forge entity renderer with no coremod or OptiFine-specific hooks; OptiFine compatibility is designed for but still requires in-game verification.
- Personal Verity companion automatically appears on a player's first join (configurable).
- Per-player owner UUID is saved with the entity; each companion follows its own player.
- Original floating golden cube model with fins, antenna, dark eyes, and mouth.
- Gentle bobbing and idle movement animation.
- Friendly follow behaviour before the third sleep.
- After the third sleep, Verity becomes a distant, non-hostile watcher that keeps its distance and looks toward the player.
- Configurable horror intensity (0–3), horror enable/disable, watcher enable/disable, and auto-spawn.
- Per-player trust and a capped two-diamond lifetime gift system.
- Mood/status command, sleep count, last sleep coordinates, current biome, dimension, last known position, and recent biome journal.
- Recent per-player conversation history is stored and included in later Gemini prompts.
- Gemini 2.5 Flash is the default model. Network calls run on a background executor with request cooldowns and timeouts.
- AI prompt uses a separate system instruction, restricts the character to Minecraft context, and prevents it from claiming it has placed blocks or items.
- Structure commands: starter hut (with one-time starter supply chest), camp, wheat farm, flower garden, and watchtower.
- In-game help and memory commands.

## Commands
- `/verity help`
- `/verity summon` — operator only; manually spawns a Verity.
- `/verity ask <message>` — asks Gemini.
- `/verity build hut`
- `/verity build camp`
- `/verity build farm`
- `/verity build garden`
- `/verity build watchtower`
- `/verity memory`
- `/verity journal`
- `/verity status`

## Gemini setup
1. Launch the mod once to generate `config/verityai-common.toml`.
2. Close Minecraft and set `apiKey` locally.
3. Keep the key private. Never commit the config or embed the key in a shared JAR.
4. The `model` setting defaults to `gemini-2.5-flash`. Change it only to a model your AI Studio key can access.
5. Restart Minecraft and try `/verity ask hello`.

API access and free-tier quotas depend on the Google AI Studio account and can change.

## Build
Use JDK 8 and Gradle 7.3.3, then run `gradle --no-daemon build`. The JAR is written to `build/libs/`. GitHub Actions also builds the mod and uploads a JAR artifact when the build succeeds.

This repository is a playable-build candidate, not a runtime-verified release, until the JAR is tested in a real 1.16.5 client and server. In particular, verify the custom texture loads, the model is correctly positioned, and OptiFine does not interfere with rendering.
