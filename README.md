# NetherRatio

Unofficial compatibility port of [ZyanKLee/NetherRatio](https://github.com/ZyanKLee/NetherRatio) for **Paper 26.3 / Java 25**.
Release: `3.0.3-paper26.3.1` (Beta). Original authors: xDxRAx, ZyanKLee and the NetherRatio Team.

[Download releases](https://github.com/vladnow/NetherRatio-Paper26.3/releases) · [Port source](https://github.com/vladnow/NetherRatio-Paper26.3)

Customize the coordinate ratio between the Overworld and Nether. Keep vanilla 8:1, use 1:1, or configure ratios and X/Z offsets for individual world pairs. Existing configuration and the five upstream message languages are retained.

## Install

1. Stop the server and back up worlds and `plugins/NetherRatio`.
2. Remove the previous NetherRatio JAR from `plugins`, then install the new JAR. Keep only one version.
3. Start Paper 26.3 with Java 25. Configure `plugins/NetherRatio/config.yml`.
4. Run `/netherratio reload`. For a global 1:1 ratio use `/netherratio set 1`.

Changing the ratio affects portal destination searches; it does not relocate existing portals. Portal placement and search still use server mechanics. Configured coordinate bounds clamp the requested coordinates; they do not check whether chunks are pregenerated or guarantee a safe landing.

## Commands and permissions

| Command | Permission |
| --- | --- |
| `/netherratio` or `/netherratio list` | `netherratio.netherratio` (operator) |
| `/netherratio set <ratio> [overworld]` | `netherratio.netherratio` (operator) |
| `/netherratio reload` | `netherratio.netherratio` (operator) |
| `/netherratio calc [x z]` | `netherratio.calc` (everyone by default; player only) |

Default configuration uses 8:1. A 1:1 example is included in `publishing/config-1to1.yml`.

## Build

Use JDK 25 and the included Gradle 9.1 wrapper:

```sh
./gradlew clean releaseBundle
```

On Windows: `./build.ps1` (or `./build.ps1 -Offline` when dependencies are cached).
The release bundle includes the plugin, a sources JAR, complete source ZIP, download ZIP and SHA-256 checksums.
The complete source ZIP contains build scripts; publish it beside the binary to satisfy GPL requirements.

Compiled against Paper `26.3.build.159-beta`. See [TESTING.md](TESTING.md) for measured verification and limitations. Spigot, Folia, older Minecraft versions and other server forks are not certified by this release.

## License and attribution

**GPLv3**, preserved from upstream [LICENSE.txt](LICENSE.txt). The upstream README says MIT, but the actual upstream license file is GPLv3. This port does not claim an MIT grant.
See [NOTICE.md](NOTICE.md) for provenance and modification notices. This is an independent port, not an official upstream release.

Publication files are in `publishing`. Marketplace acceptance is separate from license permission; see `publishing/PUBLICATION.ru.md`. No remote upload is performed by the build.
