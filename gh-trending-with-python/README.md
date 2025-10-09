# gh-trending-with-python

This project tracks trending repositories on GitHub using Python.

## Installation

To install all required libraries, use the `requirements.txt` file. It lists all dependencies needed to run the project.

### Using `requirements.txt`

1. (Optional) Create and activate a virtual environment:
   ```sh
   python3 -m venv venv
   source venv/bin/activate
   ```

2. Install dependencies:
   ```sh
   pip install -r requirements.txt
   ```
3. Run the script:
   ```sh
   python src/main.py repos --since weekly --spoken-language en
   python main.py repos --language python --since weekly --spoken-language en 
   ```

## Notes
- Make sure you are using Python 3.7 or higher.
- All dependencies are managed via `requirements.txt`.
- For development, it is recommended to use a virtual environment to avoid conflicts with system packages.

