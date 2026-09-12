package br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools;

import java.io.IOException;

public interface ResourceFileService {
    String read(final String resourcePath) throws IOException;
}
