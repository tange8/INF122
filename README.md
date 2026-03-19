# INF122

## Run the game (Windows / PowerShell)

1. (Optional) Compile the code into `out/`:
```powershell
$files = Get-ChildItem -Recurse -Filter *.java .\src\main
mkdir out -Force | Out-Null
javac -d out $files.FullName
```

2. Create the jar manifest (this must be saved to a file):
```powershell
Set-Content -Path manifest.txt -Value "Main-Class: main.GMAECLI" -Encoding ascii
```

3. Build the jar:
```powershell
jar cfm GuildQuest.jar manifest.txt -C out main
```

4. Run it:
```powershell
java -jar GuildQuest.jar
```

## Gameplay controls

Input player profiles and select a mini-adventure when prompted.

Player movement uses cardinal directions:
`N` = North, `S` = South, `E` = East, `W` = West.
