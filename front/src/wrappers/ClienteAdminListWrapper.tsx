// lista de clientes con logica de edicion, baja y alta

import { useState } from "react";
import ClienteAdminList from "../components/ClienteAdminList";
import FormularioEdicionCliente from "../components/FormularioEdicionCliente";
import { useClientes } from "../hooks/useClientes";
import { ClienteAdminDTO } from "../types/Cliente";

const ClienteAdminListWrapper = () => {
  const {
    clientes,
    loadingClientes,
    handleDarDeBaja,
    handleDarDeAlta,
    handleEditarCliente,
  } = useClientes();

  const [clienteEditando, setClienteEditando] =
    useState<ClienteAdminDTO | null>(null);

  if (loadingClientes) return <p>Cargando clientes...</p>;

  const abrirEdicion = (cliente: ClienteAdminDTO) => {
    setClienteEditando(cliente);
  };

  const guardarEdicion = async (clienteEditado: ClienteAdminDTO) => {
    await handleEditarCliente(clienteEditado);
    setClienteEditando(null);
  };

  const cancelarEdicion = () => {
    setClienteEditando(null);
  };

  return (
    <>
      <ClienteAdminList
        activos={clientes.activos}
        dadosDeBaja={clientes.dadosDeBaja}
        onDarDeBaja={handleDarDeBaja}
        onDarDeAlta={handleDarDeAlta}
        onEditar={abrirEdicion}
      />

      {clienteEditando && (
        <FormularioEdicionCliente
          cliente={clienteEditando}
          onGuardar={guardarEdicion}
          onCancelar={cancelarEdicion}
        />
      )}
    </>
  );
};

export default ClienteAdminListWrapper;
