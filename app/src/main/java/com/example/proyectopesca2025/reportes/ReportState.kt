package com.example.proyectopesca2025.reportes

import com.example.proyectopesca2025.database.Reporte

data class ReportState(
    val reportTitle: String = "",
    val reportDescription: String = "",
    val reportDate: String = "",
    val report: List<Reporte> = emptyList()
)