# Dettagli per sviluppatori

# Development Details

## Project Structure

- src/
    - `model/` 
    - `view/` 
    - `controller/` 
    - `config/` # Configurationi e costanti
    - `Main` # lancia l'applicazione
    - `MvcManager` # accoppia i componenti MVC per permettere la comunicazione tra essi

# DA IMPLEMENTARE

## VISIVO
### Menu iniziale
percorso con i livelli (ad esempio 5 pallini ogni pallino un livello)
- bottone saga -> livelli fatti da noi
- bottone editor -> switcha editor livello
- coiunter [stelline](#stelle) 

[//]: # (Aggiungere foto dal gruppo)

### Editor Livello
permettere la creazione del livello all'utente. 


### Impostazioni APP
- schermo intero, resizable, ecc... 
- lingua 
- help comandi gioco
- musica -> on/off , scegliere tema musica
- sound effects
- comandi

### Impostazioni durante il gioco
Se premi ESC si aprono impostazioni gioco.
- storico livello 
- uscire / restar livello
- musica -> on/off , scegliere tema musica
- sound effects 
- comandi
- RESUME -> toorni a giocare (anche se ripremi ESC)

### Shop
nello shop è possibile comprare: 
- avatar

## GIOCO 
- label tempo

### Descrizione Gameplay 
la creature deve completare il labirinto. 
La fine del labirinto è il blocco in cui sta la caramella.

Durante il percorso è possibile mangiare dolci 'extra' (torta??) 
non sono fondamentali se presi tutti danno una stella in più

### vita
creatura ha 1 sola vita

### blocchi 
Un blocco nella mappa può essere:
- SPAZIO -> ci può passare la creatura
- CREATURA
- CARAMELLA (solo 1 per mappa)
- MURO -> blocca la creatura
- DOLCE EXTRA (torta??) -> per platinare il livello
- TRAPPOLA1-> se la tocchi muroi
- TRAPPOLA2 : si attiva al primo tocco -> parte timer in cui rimane attiva -> se tocchi quando è attiva muori
- TRAPPOLA3 : si attiva con un timer ciclcio. se la tocchi muori.
- NEMICO1 -> si muove in direzione pre impostata, se la tocchi muori
- NEMICO2 -> sta fermo in un blocco. Spara in direzione pre impostata.






### STELLE
ogni livello ha un rating da 0 - 3 stelle
0 -> non completato 

Aggiungere una stella se 
- completato 
- completato in tempo 
- preso tutte le torte

Le stelle sono spendibili nello [shop](#shop)





