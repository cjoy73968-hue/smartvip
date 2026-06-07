# AGENTS.md

## OpenSpec Workflow

This repo uses OpenSpec's "spec-driven" schema for change management: **proposal → specs → design → tasks → implementation**.

Core workflow:
```
/opsx-propose <name>   # Create change with all artifacts
/opsx-apply [name]      # Implement tasks from a change
/opsx-archive [name]    # Archive a completed change
/opsx-explore [name]    # Thinking partner (read-only)
```

**Requires `openspec` CLI** - all commands depend on it.

## Change Structure

Each change lives at `openspec/changes/<name>/` with:
- `proposal.md` — what & why
- `specs/` — capability specs
- `design.md` — how
- `tasks.md` — implementation checklist (`- [ ]` / `- [x]`)

Archived changes move to `openspec/changes/archive/YYYY-MM-DD-<name>/`.

## Commands vs Skills

- **Commands** (`/opsx-*`): User-facing slash commands
- **Skills** (via Skill tool): Same logic for subagent invocation

Both sources are authoritative—read the `.md` files in `.opencode/commands/` or `.opencode/skills/*/SKILL.md` for exact behavior.

## Current Project Context

This repo contains requirements for a **WeChat mini-program membership management system** (微信小程序会员管理系统). The `docs/` directory holds the business requirements. No application code exists yet.