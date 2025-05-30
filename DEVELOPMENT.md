# Dettagli per sviluppatori

## Come lavorare al progetto

### Aggiungere un oggetto view
1. Creare una classe che implementa [ViewComp](src/view/ViewComp.java)
2. Implementare il metodo `bindController` richiesto dalla java interface, 
questo metodo verrà chiamato dalla interfaccia [Controller](src/controller/Controller.java)
per legare il controller specifico alla specifica view.
3. Se necessario (ad esempio è una view può essere mostrata dal controller, come un menù collegato a un button) , aggiungere il viewComponent all'interfaccia [View](src/view/View.java).

**Ricorda che se necessario, un viewComponent deve poter essere richiamato *solamente* attraverso l'interfaccia [View](src/view/View.java).**

### Aggiungere un oggetto controller
1. Creare una classe che implementa [ControllerObj](src/controller/ControllerObj.java).
2. Attualmente questa interfaccia serve solo come 'marker'.
Nel dettaglio, l'interfaccia al modulo [Controller](src/controller/Controller.java) necessita di un type comune a tutti gli oggetti controller.
3. Per far si che l'oggetto controller venga legato al componente view:
   - Implementare come l'oggetto controller viene legato alla view nel metodo `bindController` in [ViewComp](src/view/ViewComp.java).
   - Chiamare `viewComponent.bindController(new ControllerObj())` nel metodo `bind` in [Controller](src/controller/Controller.java). *(Sostituire controllerObj e viewComponent con i nomi veri)*

### Aggiungere un oggetto model
Attualmente non è necessario implementare o estendere alcuna classe per gli ogetti model.

**Ricorda che se necessario, un oggetto model deve poter essere richiamato *solamente* attraverso l'interfaccia [Model](src/model/Model.java).**
## Project Structure

- src/
    - `model/` 
    - `view/` 
    - `controller/` 
    - `config/` # Configurationi e costanti
    - `Main` # lancia l'applicazione


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





