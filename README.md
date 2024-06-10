# CatacombeDelReCaduto
Gioco d'avventura testuale ambientato nelle oscure catacombe del Re caduto del regno di Arindor.

### Requisiti
Versione di Java minima : Java 16.

### Configurazione AWS s3 bucket
Per giocare salvando su AWS s3 bucket è necessario creare e configurare un file "awsConnectionSettings.json" nella cartella di avvio del programma,
vedi il file d'esempio "CatacombeDelReCaduto/awsConnectionSettings.json".

### Documentazione
La documentazione del progetto è nella cartella "CatacombeDelReCaduto/documentation".

#### Note sui design class models
Il package diagram rappresenta le interazioni ad alto livello tra i vari package del programma, senza evidenziare le classi che li compongono.
I diagram dei vari package (items, entities, game, prompts,jsonHandler, menù) rappresentano invece le interazioni che ogni classe che compone il determinato package ha con il resto del programma.
Mettendo insieme i diagrammi dei vari package si ottiene il design class model completo. La suddetta divisione del diagramma delle classi completo nei vari sottodiagrammi é stata fatta per fini di chiarezza e maggior leggibilità.
Sempre per questo motivo nei sottodiagrammi sono state rimosse le cardinalità e i nomi delle varie associazioni, che si possono però trovare nel diagramma completo.

### Avvio del gioco
Il gioco è avviabile dal file "out/artifacts/CatacombeDelReCaduto_jar/CatacombeDelReCaduto.jar"
tramite cmd con il comando : "java -jar CatacombeDelReCaduto.jar" o se supportato con il doppio click sul file.

### Istruzioni sul gioco
Una volta iniziata una partita è possibile avere una lista dei comandi tramite il comando "help",
lo scopo del gioco è battere il boss che si trova alla fine della storia.
Nel gioco non è presente il salvataggio automatico e quando si muore il gioco viene ripreso dall'ultimo salvataggio effettuato.

Buona fortuna avventuriero.
