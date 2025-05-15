# The Loss



---

## 💻Tecnologie utilizzate:

Le tecnologie utilizzate sono: ***Java, Gradle, LibGDX, Gdx-AI, Box2D. GLSL (Graphics Library Shader Language), Tiled.***

---

## 📃 Caratteristiche :

Il game engine contiene seguenti funzionalità:
Sistema automatizzato di gestione delle entità, implementazione della fisica tramite
engine fisico (Box2D), gestione delle entità tramite ECS custom, gestione degli eventi
personalizzati tramite sensori dell’engine fisico, serializzazione progresso di gioco /
configurazione impostazioni, sistema di combattimento e interazione, sistema
asincrono dell’intelligenza artificiale delle entità, path-finding tramite A star (A*)
pesato dinamicamente.

---

## 📸Schermate


### - 🏠Main menu
![alt](/screenshots/home.png)

---

### -⚙️Settings
![alt](/screenshots/settings.png)

---

## -🔃Loading menu
![alt](/screenshots/loading.png)

---

## -☠️Death Screen
![alt](/screenshots/death2.png)

---

## -🎮Game Screen
![alt](/screenshots/gamescreen.png)

---

##  Requisiti

Un computer funzionante, con schermo e tastiera (possibilmente mouse), se si vuole avviare il jar si consiglia ***Java 21.***

## 🧰 Installazione e Avvio (locale)

Questa guida spiega come eseguire il gioco localmente sulla propria macchina utilizzando Java e libGDX.

---

### ✅ Prerequisiti

Assicurati di avere installato i seguenti strumenti:

- **Java Development Kit (JDK) 21 o superiore**  
  Scaricabile da [Adoptium](https://adoptium.net/) o [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).

- **Un IDE compatibile** (opzionale, ma consigliato):  
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [Visual Studio Code](https://code.visualstudio.com/)
  - [Eclipse](https://www.eclipse.org/) (meno consigliato con libGDX)

- **Git** (per clonare il repository, se necessario)  
  Scaricabile da [git-scm.com](https://git-scm.com/)

---

### 📦 Clonazione del progetto

Clonare la repository:

```bash
git clone https://github.com/NOME-UTENTE/NOME-REPO.git
```

### ▶️ Avvio

Importarlo su Eclipse, IntelliJ, VScode.
Se non viene buildato da solo, eseguire il build di gradle utilizzare il comando:

```bash
.\gradlew clean build
```

Avviare il programma tramite dalla classe main.
