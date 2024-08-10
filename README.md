# Taaza Khabar

**Taaza Khabar** is a Spring Boot application that automates the process of capturing, processing, and delivering screenshots from the Greater Kashmir and Greater Kashmir Uzma e-papers. The application captures screenshots of these e-papers, converts them into separate PDFs, and then sends them to users via email. This entire process is managed through CRON scheduling to ensure timely delivery.

## Features

- **User Management**: 
  - Create and verify user accounts.
  - Send verification emails to users.
  - Delete user accounts.

- **Web Scraping and Screenshot Generation**:
  - Automatically scrape the Greater Kashmir and Greater Kashmir Uzma e-papers.
  - Capture screenshots of both English and Urdu e-papers.
  - Convert the captured screenshots into separate PDF files.

- **Automated Email Delivery**:
  - Send the generated PDF files to users via email.
  - Schedule the scraping and email delivery using CRON jobs to ensure timely distribution.

## Project Structure

- `src/main/java/dev/shaukat/Taaza_Khabar/`: Contains the main application logic.
  - `TaazaKhabarApplication.java`: The entry point of the application.
  - `api/controller/UserController.java`: Handles user-related HTTP requests.
  - `greaterkashmir/GreaterKashmirScraper.java`: Contains the logic for scraping the Greater Kashmir website and capturing screenshots.

- `src/main/resources/`: 
  - `application.properties`: Configuration for the application.
  - `schema.sql`: SQL schema for the application's database.

## Getting Started

### Prerequisites

- Java 8 or above.
- Maven 3.6.3 or above.
- A running PostgreSQL instance.
- Chrome WebDriver for Selenium to capture screenshots.

### Installation

1. Clone the repository:
   git clone https://github.com/shaukatishtiaq/taaza_khabar.git
   cd taaza_khabar
   
2. Configure the application by modifying application.properties to suit your environment.
Set up the database:
  psql -h <host> -U <user> -d <database> -a -f src/main/resources/schema.sql

3. Install Chrome WebDriver and ensure it's accessible from your system's PATH.

4. Build and run the application:
  mvn clean install
  java -jar target/taaza_khabar-0.0.1-SNAPSHOT.jar

## Usage
### API Endpoints:

- `POST /users: Create a new user.`
- `GET /users/{email}/verify: Send verification email to the user.`
- `GET /users/{email}/verify/{verificationCode}: Verify a user with the provided verification code.`
- `DELETE /users/{email}: Delete a user.`

### Web Scraping and PDF Generation:

The application automatically scrapes the Greater Kashmir and Greater Kashmir Uzma e-papers and stores screenshots as PNG files.
These screenshots are then converted into PDFs, which are sent to users via email.

### CRON Scheduling:

The scraping, PDF generation, and email delivery are all managed by CRON jobs, ensuring that users receive the latest e-papers on schedule.

## Note
This project was created solely for learning purposes and is not intended for any commercial use.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request for review.
