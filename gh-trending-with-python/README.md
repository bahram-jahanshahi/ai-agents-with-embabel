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

3. Run API server:
   ```sh
   # from project root
    uvicorn src.api:app --reload --port 8000
   ``` 
## Usage
### Open the interactive docs:
Swagger UI: http://127.0.0.1:8000/docs   
ReDoc: http://127.0.0.1:8000/redoc
### Example API requests:

- Get trending repositories:
  ```shell
  # All languages, weekly, English spoken repos
   curl 'http://127.0.0.1:8000/repos?since=weekly&spoken_language=en'
   
   # Python repos, weekly, English spoken
   curl 'http://127.0.0.1:8000/repos?language=python&since=weekly&spoken_language=English'
   
   # Developers (no spoken language filter here)
   curl 'http://127.0.0.1:8000/developers?language=rust&since=monthly'
   
   # Discover filters
   curl 'http://127.0.0.1:8000/languages'
   curl 'http://127.0.0.1:8000/spoken-languages'

   ```
## Docker
shell commands to build and run the Docker container:
```sh
docker build -t gh-trending-api .
docker run -p 8000:8000 gh-trending-api
```

## Notes
- Make sure you are using Python 3.7 or higher.
- All dependencies are managed via `requirements.txt`.
- For development, it is recommended to use a virtual environment to avoid conflicts with system packages.

