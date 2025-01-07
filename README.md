# Library Management System Desktop Application #

This project is a Desktop Library Management Application, developed as a part of the Object-Oriented Programming (OOP) course during Year 2, Semester 1 at UET - VNU.

## 📋 Overview ##

The application serves as a complete library management system, designed to streamline book borrowing, payment processing, and transaction tracking. It leverages several modern APIs for enhanced functionality and real-world integration.

✨ Key Features
1. Search Books with Google Books API
Users can search for books using the Google Books API.
Users can add books directly to the library system from the search results.
2. Generate Payment QR Codes Automatically with VietQR API
The application integrates with the VietQR API to create payment QR codes for overdue book fines or library membership fees.
Users can scan the generated QR code with banking apps for quick and secure payments.
3. Transaction History Reading with Casso API
The application fetches transaction history from a linked bank account using the Casso API.
Automatically verifies payments and updates the user’s account status in the library system.
🛠️ Technologies Used
Programming Language: Java
Framework: JavaFX for desktop UI
APIs:
Google Books API for book searches
VietQR API for payment QR generation
Casso API for reading bank transaction history
Database: SQLite for local data storage
🚀 Setup Instructions
Clone the Repository

bash
Sao chép mã
git clone https://github.com/your-username/library-management-app.git  
Install Dependencies
Ensure you have the following installed on your system:

Java Development Kit (JDK) 8 or later
Maven (for dependency management)
API Key Configuration

Obtain API keys for the following:
Google Books API: Get started here
VietQR API: Contact VietQR for API access.
Casso API: Get started here
Add the API keys to the config.properties file in the project directory.
Run the Application

bash
Sao chép mã
mvn javafx:run  
📚 Usage
Searching for Books
Navigate to the “Search Books” section.
Enter the book title or author in the search bar.
Select a book from the results and add it to the library.
Processing Payments
Go to the “Payments” section.
Enter the payment details (e.g., amount, purpose).
Generate a QR code and scan it using your banking app.
Viewing Transaction History
Open the “Transaction History” tab.
Fetch and view bank transactions using the Casso API.
Verify and update user payments in the system.
🌟 Contribution
Contributions are welcome! Feel free to fork this repository and submit a pull request for any improvements or bug fixes.

📞 Contact
Developer: [Your Name]
Email: your.email@uet.vnu.edu.vn
University: University of Engineering and Technology (UET), Vietnam National University (VNU)
This project demonstrates the practical application of Object-Oriented Programming principles in developing real-world applications. Designed for academic purposes, it is a starting point for integrating APIs and managing complex desktop systems.
