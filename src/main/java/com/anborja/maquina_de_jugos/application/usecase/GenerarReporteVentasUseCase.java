package com.anborja.maquina_de_jugos.application.usecase;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.port.outbound.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerarReporteVentasUseCase {

    private final JugoRepositoryPort repository;

    public record ReporteVentas(
            LocalDate fecha,
            int jugosPreparando,
            int jugosListos,
            int jugosEntregados,
            Map<String, Long> saboresMasVendidos,
            Map<String, Long> tamanosMasVendidos,
            double ingresosTotales
    ) {}

    public ReporteVentas ejecutar() {
        System.out.println("📊 === GENERANDO REPORTE DE VENTAS ===");

        // Usar métodos existentes del repository
        List<JugoEntity> preparando = repository.buscarPorEstado("PREPARANDO");
        List<JugoEntity> listos = repository.buscarPorEstado("LISTO");
        List<JugoEntity> entregados = repository.buscarPorEstado("ENTREGADO");

        // Estadísticas de sabores más vendidos
        Map<String, Long> saboresMasVendidos = entregados.stream()
                .collect(Collectors.groupingBy(JugoEntity::getSabor, Collectors.counting()));

        // Estadísticas de tamaños más vendidos
        Map<String, Long> tamanosMasVendidos = entregados.stream()
                .collect(Collectors.groupingBy(JugoEntity::getTamanoVaso, Collectors.counting()));

        // Ingresos totales
        double ingresosTotales = entregados.stream()
                .mapToDouble(JugoEntity::getPrecio)
                .sum();

        ReporteVentas reporte = new ReporteVentas(
                LocalDate.now(),
                preparando.size(),
                listos.size(),
                entregados.size(),
                saboresMasVendidos,
                tamanosMasVendidos,
                ingresosTotales
        );

        System.out.println("📈 Reporte generado - Total entregados: " + entregados.size() +
                ", Ingresos: $" + ingresosTotales);

        return reporte;
    }
}