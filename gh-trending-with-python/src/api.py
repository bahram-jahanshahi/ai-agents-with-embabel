from typing import Optional, List, Dict
from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware

from gtrending import (
    fetch_repos,
    fetch_developers,
    languages_list,
    spoken_languages_list,
    check_language,
    check_since,
    check_spoken_language_code,
)

app = FastAPI(title="GitHub Trending API (gtrending wrapper)", version="1.0.0")

# If you’ll call this from a browser/frontend, enable CORS (tighten in prod).
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # change to your domain(s) for production
    allow_credentials=True,
    allow_methods=["GET"],
    allow_headers=["*"],
)

VALID_SINCE = {"daily", "weekly", "monthly"}


def resolve_spoken_language_code(user_input: Optional[str]) -> Optional[str]:
    """
    Accepts either a code (e.g., 'en', 'ES') or a language name (e.g., 'English', 'Español').
    Returns a lowercase ISO 639-1 code if resolvable, else None.
    """
    if not user_input:
        return None
    s = user_input.strip()

    # If it's plausibly a code, validate directly
    if len(s) in (2, 3) and check_spoken_language_code(s):
        return s.lower()

    # Match by language name (case-insensitive) against gtrending's list
    target = s.casefold()
    for item in spoken_languages_list():
        code = item.get("code")
        names: List[str] = item.get("name", [])
        if any(target == n.casefold() for n in names):
            return code.lower()

    return None


@app.get("/languages")
def get_languages() -> List[Dict]:
    """Return supported programming languages (param + human name)."""
    return languages_list()


@app.get("/spoken-languages")
def get_spoken_languages() -> List[Dict]:
    """
    Return supported spoken languages.
    Each item typically has: {"code": "en", "name": ["English", "Inglés", ...]}
    """
    return spoken_languages_list()


@app.get("/repos")
def get_repos(
    language: Optional[str] = Query(None, description="Programming language (omit for all languages)"),
    since: str = Query("daily", description="daily | weekly | monthly"),
    spoken_language: Optional[str] = Query(
        None,
        description="Spoken language code (e.g., 'en', 'sv') or name (e.g., 'English', 'Svenska').",
    ),
):
    """
    Fetch trending repositories from GitHub Trending via gtrending.
    - Omit 'language' to get all programming languages.
    - 'spoken_language' applies only to repos (GitHub Trending feature).
    """
    # Validate 'since'
    if since not in VALID_SINCE or not check_since(since):
        raise HTTPException(status_code=400, detail="Invalid 'since'. Use one of: daily, weekly, monthly.")

    # Validate language (if provided)
    if language and not check_language(language):
        raise HTTPException(status_code=400, detail=f"Unknown programming language: {language!r}.")

    # Resolve spoken language (code or name accepted)
    spoken_code = None
    if spoken_language:
        spoken_code = resolve_spoken_language_code(spoken_language)
        if not spoken_code or not check_spoken_language_code(spoken_code):
            raise HTTPException(
                status_code=400,
                detail=(
                    f"Invalid spoken language {spoken_language!r}. "
                    "Call /spoken-languages to see supported codes/names."
                ),
            )

    data = fetch_repos(language=language, since=since, spoken_language_code=spoken_code)
    return {"count": len(data), "items": data}


@app.get("/developers")
def get_developers(
    language: Optional[str] = Query(None, description="Programming language (omit for all languages)"),
    since: str = Query("daily", description="daily | weekly | monthly"),
):
    """
    Fetch trending developers from GitHub Trending via gtrending.
    Note: GitHub Trending developers endpoint does NOT support spoken-language filtering.
    """
    # Validate 'since'
    if since not in VALID_SINCE or not check_since(since):
        raise HTTPException(status_code=400, detail="Invalid 'since'. Use one of: daily, weekly, monthly.")

    # Validate language (if provided)
    if language and not check_language(language):
        raise HTTPException(status_code=400, detail=f"Unknown programming language: {language!r}.")

    data = fetch_developers(language=language, since=since)
    return {"count": len(data), "items": data}
