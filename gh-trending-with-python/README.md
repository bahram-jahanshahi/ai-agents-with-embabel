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
    or
    ```sh
   # Print weekly trending Python repos where the README/etc is in English
    python src/main.py repos --language python --since weekly --spoken-language en
    
    # Same, but save to CSV
    python src/main.py repos -l python -s weekly -S English -f csv -o out/python-weekly-en.csv
    
    # Trending developers for Rust (developers endpoint does not use spoken language)
    python src/main.py developers -l rust -s monthly -f json -o out/rust-devs-monthly.json
    
    # Discover available language filters
    python src/main.py list-langs | head -n 20
    python src/main.py list-spoken-langs | grep -i "swedish"

    ```

## Notes
- Make sure you are using Python 3.7 or higher.
- All dependencies are managed via `requirements.txt`.
- For development, it is recommended to use a virtual environment to avoid conflicts with system packages.

