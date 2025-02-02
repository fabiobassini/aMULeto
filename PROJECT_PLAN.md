# Project Plan

### **Software Engineering Management (Capitolo 2)**

1. **Project Plan**
    - **Obiettivi del progetto**: Realizzare un'applicazione che generi diagrammi UML a partire da un progetto Java, con un'interfaccia grafica per la visualizzazione e modifiche.
    - **Risultati attesi**: Il prodotto finale consiste in un'app che crea diagrammi UML (Class Diagram), salva i file SVG, consente l'interazione tramite una GUI.
    - **Responsabili**: Fabio Bassini (Tech Lead).
    - **Modello di processo**: Ho applicato una metodologia **Agile** per migliorare la gestione del tempo e la qualità del software.
    - **Organizzazione del progetto**: Ho gestito il progetto individualmente.
    - **Standard, linee guida e procedure**: Ho utilizzato convenzioni di codifica standard per Java con pattern MVC e convenzioni di documentazione.
    - **Gestione del rischio**:
        - Uno dei principali rischi nel progetto è la possibilità di bug nell'interfaccia utente (UI), che potrebbe compromettere l'esperienza dell'utente. Per affrontare questo rischio, sono stati effettuati test regolari dell'interfaccia per verificare la corretta visualizzazione dei diagrammi UML e l'usabilità dei controlli, come i pulsanti e le finestre di dialogo.
        - Un altro rischio identificato è relativo agli errori nel parsing dei file Java, che potrebbero compromettere la corretta generazione del diagramma UML. Per mitigarne il rischio, sono stati creati casi di test specifici per i file Java con vari formati di codice e strutture, assicurando che il parser gestisse correttamente tutte le casistiche.
    - **Gestione del personale**: Ho gestito il progetto individualmente.
    - **Metodi e tecniche**: Utilizzo di **Java**, **JavaParser**, **PlantUML**, **Batik**.
    - **Garanzia di qualità**: Per garantire la qualità del software, sono stati implementati test di unità per verificare il corretto funzionamento delle singole componenti del sistema, come il parsing dei file Java e la generazione dei diagrammi UML. Questi test sono stati scritti utilizzando framework come JUnit, assicurando che le principali funzioni e metodi del sistema fossero testati in isolamento per rilevare eventuali bug a livello di codice. Inoltre, sono stati sviluppati test di accettazione per convalidare che l'intero flusso del software, dall'importazione dei file Java alla generazione del diagramma UML, rispettasse le specifiche richieste. Questi test sono stati progettati per simulare l'uso reale del sistema da parte degli utenti finali, verificando che il software soddisfacesse le aspettative di funzionalità, usabilità e performance. Entrambi i tipi di test sono stati eseguiti regolarmente, e i risultati sono stati utilizzati per correggere i problemi e migliorare continuamente la qualità del software.
    - **Work packages**: Il progetto aMULeto può essere suddiviso in diverse attività principali, ognuna delle quali rappresenta un pacchetto di lavoro che può essere gestito in modo indipendente. Ecco una proposta di suddivisione:
        1. **Parsing dei file Java**  
            - **Attività**: Creazione di un modulo per scansionare e analizzare i file Java utilizzando JavaParser. Il modulo dovrebbe essere in grado di leggere la struttura delle classi, interfacce, metodi e attributi.    
            - **Deliverable**: Codice che estrae correttamente le classi Java da un progetto, comprese le relazioni tra di esse.
        2. **Generazione diagrammi UML**  
            - **Attività**: Implementazione della logica per generare il diagramma UML a partire dal codice Java. Include la rappresentazione delle classi, metodi, attributi, e le relazioni tra di esse (associazioni, ereditarietà, ecc.). Questo deve generare un file .puml utilizzando PlantUML.    
            - **Deliverable**: Diagrammi UML generati correttamente che riflettono la struttura del progetto Java.
        3. **Sviluppo GUI**  
            - **Attività**: Creazione di un'interfaccia grafica utente (GUI) che consenta all'utente di caricare un progetto Java, visualizzare il diagramma UML generato e interagire con esso. La GUI dovrà anche includere funzionalità per la visualizzazione e modifica dei diagrammi.  
            - **Deliverable**: Interfaccia utente completamente funzionante che permetta agli utenti di interagire con il sistema.
        4. **Salvataggio e visualizzazione SVG**  
            - **Attività**: Creazione di una funzionalità per generare e salvare il diagramma UML come file SVG, integrando Batik per la visualizzazione SVG.  
            - **Deliverable**: Diagrammi UML salvati come file SVG con supporto per zoom e pan.
        5. **Test e Debugging**  
            - **Attività**: Creazione di test di unità per il parsing, la generazione dei diagrammi e l'interazione con la GUI. Debugging per risolvere i bug rilevati durante i test.   
            - **Deliverable**: Test di unità e codice stabile privo di errori.
    - **Risorse**: 
        - **Hardware**:  
            - PC con almeno 8 GB di RAM, processore moderno (Intel i5/i7 o equivalente).  
            - Sistema operativo: Windows, macOS o Linux.  
        - **Software**:  
            - IDE: Eclipse, IntelliJ IDEA o Visual Studio Code.  
            - Librerie e strumenti:  
                - Java 8+ (Java Development Kit).  
                - JavaParser per il parsing del codice Java.  
                - PlantUML per la generazione dei diagrammi UML.  
                - Batik per la visualizzazione SVG.  
                - Maven per la gestione delle dipendenze e il build del progetto.  
                - Git per il controllo delle versioni e GitHub per il repository del codice.  
            - **Tools di testing**:  
                - JUnit per i test di unità.  
                - SonarLint per il controllo della qualità del codice.
    - **Budget e tempistiche**: 
        - **Budget**:  
            - Costo di sviluppo: Il progetto è principalmente a livello software, quindi il costo principale è per le risorse di sviluppo e gestione.  
            - Strumenti: L'uso di Maven, GitHub e altre librerie open-source riduce significativamente i costi.  
        - **Tempo di sviluppo**: Stimato in 120-170 ore di lavoro a seconda della complessità delle attività e della disponibilità delle risorse.  
    - **Gestione delle modifiche**: 
        - Ho eseguito tutti i test in locale in autonomia, verificando il corretto funzionamento delle funzionalità implementate. 
        - Le modifiche sono state gestite in modo iterativo e sono state inviate tramite git push alla branch di sviluppo.  
        - Le modifiche sono state gestite in modo iterativo e sono state inviate tramite git push alla branch di sviluppo.
        - I cambiamenti vengono monitorati e documentati tramite commit dettagliati, in modo da mantenere sempre aggiornata la storia del progetto. 
        - Il progresso viene verificato periodicamente, con eventuali aggiornamenti del project board per riflettere le modifiche e le nuove funzionalità.
    - **Consegna**: 
        - **Rilascio del software**: Il rilascio finale del software sarà effettuato tramite **GitHub Releases**, dove verrà fornito il file JAR eseguibile.  
        - Sarà inclusa una **documentazione completa** in formato markdown o PDF, che descrive il progetto, l'architettura, i diagrammi e i test.