# Installing Maven on Windows

You need to install Maven to run the Screen Engine backend. Here are three methods:

## Method 1: Using Chocolatey (Recommended)

If you have Chocolatey installed:

```bash
choco install maven
```

Verify installation:
```bash
mvn -version
```

## Method 2: Manual Installation

### Step 1: Download Maven

1. Go to https://maven.apache.org/download.cgi
2. Download `apache-maven-3.9.6-bin.zip` (or latest version)
3. Extract to `C:\Program Files\Apache\maven`

### Step 2: Set Environment Variables

1. Press `Win + X` and select "System"
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "System Variables", click "New":
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven`
5. Find the `Path` variable, click "Edit"
6. Click "New" and add: `%MAVEN_HOME%\bin`
7. Click "OK" on all dialogs

### Step 3: Verify Installation

Open a new terminal and run:
```bash
mvn -version
```

You should see:
```
Apache Maven 3.9.6
Java version: 17.x.x
```

## Method 3: Using Scoop

If you have Scoop installed:

```bash
scoop install maven
```

## Install Java (if not already installed)

Maven requires Java 17+.

1. Download from https://adoptium.net/
2. Install "Eclipse Temurin JDK 17"
3. Verify: `java -version`

## After Installation

Once Maven is installed, you can run the backend:

```bash
cd backend
mvn spring-boot:run
```

## Troubleshooting

### Issue: JAVA_HOME not set

Set JAVA_HOME environment variable:
1. Find Java installation path (usually `C:\Program Files\Eclipse Adoptium\jdk-17.x.x`)
2. Add environment variable:
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x`

### Issue: Maven command not found after installation

1. Close and reopen your terminal
2. Verify environment variables are set correctly
3. Try running from a new terminal window

---

**After installing Maven, refer back to [QUICKSTART.md](QUICKSTART.md) to run the application.**
