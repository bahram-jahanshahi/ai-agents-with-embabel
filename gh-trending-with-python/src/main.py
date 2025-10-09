#!/usr/bin/env python3
import argparse
import json
from pathlib import Path
from typing import Optional, List, Dict

from gtrending import (
    fetch_repos,
    fetch_developers,
    spoken_languages_list,
    check_language,
    check_since,
    check_spoken_language_code,
)

import pandas as pd


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

    # Try to match by language name (case-insensitive) against gtrending's list
    target = s.casefold()
    for item in spoken_languages_list():
        code = item.get("code")
        names: List[str] = item.get("name", [])
        # names can be a list of aliases; compare casefolded
        if any(target == n.casefold() for n in names):
            return code.lower()

    return None


def validate_inputs(language: Optional[str], since: str, spoken_lang_code: Optional[str]):
    if language and not check_language(language):
        raise SystemExit(f"Unknown programming language: {language!r}. Try --list-langs to see options.")
    if not check_since(since):
        raise SystemExit("Invalid --since. Use one of: daily, weekly, monthly.")
    if spoken_lang_code and not check_spoken_language_code(spoken_lang_code):
        raise SystemExit(f"Invalid spoken language code: {spoken_lang_code!r}. Try --list-spoken-langs.")


def write_output(data: List[Dict], out: Optional[str], fmt: str):
    if not out:
        # pretty print JSON if no output path
        print(json.dumps(data, indent=2, ensure_ascii=False))
        return

    out_path = Path(out)
    out_path.parent.mkdir(parents=True, exist_ok=True)

    if fmt == "json":
        out_path.write_text(json.dumps(data, indent=2, ensure_ascii=False), encoding="utf-8")
        print(f"Wrote {out_path}")
    elif fmt == "csv":
        # Normalize nested fields for repos/developers
        df = pd.json_normalize(data)
        df.to_csv(out_path, index=False)
        print(f"Wrote {out_path}")
    elif fmt in ("xlsx", "excel"):
        df = pd.json_normalize(data)
        df.to_excel(out_path, index=False)
        print(f"Wrote {out_path}")
    else:
        raise SystemExit(f"Unsupported --format: {fmt}")


def main():
    p = argparse.ArgumentParser(description="Fetch GitHub Trending via gtrending.")
    sub = p.add_subparsers(dest="entity", required=True)

    common = argparse.ArgumentParser(add_help=False)
    common.add_argument("--language", "-l", help="Programming language filter (e.g., python, rust).")
    common.add_argument("--since", "-s", default="daily", help="daily | weekly | monthly (default: daily)")
    common.add_argument("--spoken-language", "-S", help="Spoken language code or name (repos only).")
    common.add_argument("--format", "-f", default="json", choices=["json", "csv", "xlsx", "excel"],
                        help="Output format (default: json).")
    common.add_argument("--out", "-o", help="Output file path. If omitted, prints to stdout.")

    # repos subcommand
    p_repos = sub.add_parser("repos", parents=[common], help="Fetch trending repositories.")
    # developers subcommand
    p_devs = sub.add_parser("developers", parents=[common], help="Fetch trending developers.")
    # helper subcommands
    sub.add_parser("list-langs", help="Show supported programming languages.")
    sub.add_parser("list-spoken-langs", help="Show supported spoken languages (code + names).")

    args = p.parse_args()

    if args.entity == "list-langs":
        from gtrending import languages_list
        for item in languages_list():
            print(f"{item['param']}\t{item['name']}")
        return

    if args.entity == "list-spoken-langs":
        for item in spoken_languages_list():
            code = item["code"]
            names = ", ".join(item.get("name", []))
            print(f"{code}\t{names}")
        return

    # Resolve spoken language (repos only)
    spoken_code = None
    if args.entity == "repos" and args.spoken_language:
        spoken_code = resolve_spoken_language_code(args.spoken_language)

    validate_inputs(args.language, args.since, spoken_code)

    if args.entity == "repos":
        data = fetch_repos(
            language=args.language,
            since=args.since,
            spoken_language_code=spoken_code
        )
        write_output(data, args.out, args.format)
    elif args.entity == "developers":
        data = fetch_developers(
            language=args.language,
            since=args.since
        )
        write_output(data, args.out, args.format)


if __name__ == "__main__":
    main()

