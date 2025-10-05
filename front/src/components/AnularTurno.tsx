import React, { useState } from "react";
import useDocumentTitle from "../hooks/useDocumentTitle";
import useAnularTurno from "../hooks/useAnularTurno";

// componente de form para anular turno usando codigo
export default function AnularTurno() {
  useDocumentTitle("Anular Turno"); // titulo pestaña

  // estado local para el codigo ingresado
  const [codigo, setCodigo] = useState("");

  // hook q maneja la logica
  const { mensaje, error, loading, anular } = useAnularTurno();

  // al enviar el form, se intenta anular el turno
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // evita recargar la pagina
    await anular(codigo); // ejecuta la logica
    if (!error) setCodigo(""); // limpia campo si no hubo error
  };

  return (
    <div>
      <h2>Anular turno</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="codigo">Codigo de anulacion:</label>
        <input
          id="codigo"
          type="text"
          value={codigo}
          onChange={(e) => setCodigo(e.target.value)}
          required
          disabled={loading}
          className="border p-1 ml-2"
        />
        <button
          type="submit"
          disabled={loading}
          className="ml-2 px-3 py-1 bg-blue-600 text-white rounded"
        >
          {loading ? "Anulando..." : "Anular"}
        </button>
      </form>
      {/* msj de exito */}
      {mensaje && <p className="text-green-600 mt-2">{mensaje}</p>}
      {/* msj de error */}
      {error && <p className="text-red-600 mt-2">{error}</p>}
    </div>
  );
}
