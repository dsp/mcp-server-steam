# MCP Steam Server

[![smithery badge](https://smithery.ai/badge/@dsp/mcp-server-steam)](https://smithery.ai/server/@dsp/mcp-server-steam)

A Model Context Protocol (MCP) server that provides Steam gaming context to AI assistants. This service integrates with the Steam API to fetch user gaming information and exposes it through the MCP protocol, allowing AI assistants to access and understand users' gaming activities and preferences.

## Installation

### Installing via Smithery

To install Steam Gaming Context Server for Claude Desktop automatically via [Smithery](https://smithery.ai/server/@dsp/mcp-server-steam):

```bash
npx -y @smithery/cli install @dsp/mcp-server-steam --client claude
```

### Using Docker (Recommended)

The easiest way to run the MCP Steam server is using Docker:

```bash
docker run --rm -i ghcr.io/dsp/mcp-server-steam:latest
```

### Configuration

The server can be configured using environment variables:

```bash
# Required configuration
STEAM_API_KEY=your_steam_api_key
```

## Development

### Prerequisites

- OpenJDK 21
- Docker (for container builds)
- Git
- [devenv.sh](https://devenv.sh)

### Setting Up Development Environment

1. Clone the repository:
   ```bash
   git clone https://github.com/dsp/mcp-steam.git
   cd mcp-steam
   ```

2. Use the development shell:
   ```bash
   devshell shell
   ```
   This will set up the required development environment with all necessary dependencies.

3. Build the project:
   ```bash
   mvn package
   ```

### Building Docker Image Locally

```bash
docker build -t mcp-server-steam .
```

## API Documentation

The server implements the Model Context Protocol (MCP) specification. For detailed API documentation, please refer to the [MCP Documentation](https://modelcontextprotocol.io).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License
