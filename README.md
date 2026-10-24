# SheepPunch
Plugin bringing back sheep punching for Minecraft b1.7.3 Poseidon (CB1060 Fork) 

---

> [!WARNING]
> This plugin was vibecoded.

---

Before shears were added in Beta 1.7, the only way to get wool from a living sheep was to hit it. The sheep dropped wool and was sheared afterwards. Beta 1.7 replaced this with shears.

This plugin brings punching back, but with only 1 wool per sheep. Shears give 2 to 4 wool, so they stay the better way to get wool.

## Features

- **Punching a sheep drops 1 wool** in the sheep's colour and shears it, like before Beta 1.7.
- **Any melee hit counts**: bare hand, sword or any other item, except shears.
- **Shears stay vanilla**: right-click shears a sheep for 2 to 4 wool. Left-clicking with shears does not give punch wool, so you can't waste a sheep by accident.
- **Vanilla drop**: the wool pops out of the sheep the same way it does when shearing.

## Requirements

- Project Poseidon (Beta 1.7.3 Server - Fork of CB1060)
  - https://github.com/retromcorg/Project-Poseidon
- Java 8

Only built against Project Poseidon. It may work on CraftBukkit 1060, but that is untested.

## Installation

1. Put `SheepPunch*.jar` into the `plugins/` folder. Get from here: https://github.com/alya-can-eat/SheepPunch/releases
2. Start the server. That's it, there are no files to set up.

## Permissions

| Permission | Default | Description |
|---|---|---|
| `sheeppunch.use` | everyone | Get wool by punching sheep |

## Notes

- **No regrowing**: sheep don't regrow wool in Beta 1.7.3. A punched sheep stays sheared, so shearing it afterwards gives nothing. That's the trade-off: 1 wool by hand now, or 2 to 4 with shears.
- **Killing a sheep**: a sheep that isn't sheared drops 1 wool on death. If the punch kills it, it still drops only 1 wool in total: the punch wool, then no death wool because it's sheared.
- **Only players**: wolves, arrows, fire and other damage don't drop wool.
- **Protection plugins**: the plugin runs after them. A hit that is cancelled (e.g. in a protected area) drops nothing.
- **Damage**: the punch still hurts the sheep like a normal hit, the same as before Beta 1.7.

## Building from source

Compile against the Project Poseidon server jar (Java 8 target):

```bash
javac --release 8 -cp poseidon.jar -d build src/ch/alya/sheeppunch/*.java
cp plugin.yml build/
jar cf SheepPunch.jar -C build .
```
