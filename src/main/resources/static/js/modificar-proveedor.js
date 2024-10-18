const API_BASE_URL = 'http://localhost:8080/api';
const proveedorId = new URLSearchParams(window.location.search).get('id'); // Obtener ID del proveedor desde la URL

document.addEventListener('DOMContentLoaded', () => {
  cargarProveedor(proveedorId);

  document.getElementById('modificar-proveedor-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    await guardarCambios();
  });
});

// Redirigir a la página principal al hacer clic en "Volver"
function volver() {
  window.location.href = 'proveedores.html';
}

async function cargarProveedor(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/proveedores/${id}`);
    const proveedor = await response.json();

    document.getElementById('proveedor-cuit-cuil').value = proveedor.cuitCuil;
    document.getElementById('proveedor-nombre').value = proveedor.nombre;
    document.getElementById('proveedor-razon-social').value = proveedor.razonSocial;
    document.getElementById('proveedor-fecha-inicio').value = formatFecha(proveedor.fechaInicio);
    document.getElementById('proveedor-fecha-fin').value = formatFecha(proveedor.fechaFin);
  } catch (error) {
    console.error('Error al cargar proveedor:', error);
  }
}

// Formatear fecha en formato YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

async function guardarCambios(id) {
  const proveedor = {
    cuitCuil: document.getElementById('proveedor-cuit-cuil').value,
    nombre: document.getElementById('proveedor-nombre').value,
    razonSocial: document.getElementById('proveedor-razon-social').value,
	fechaInicio: document.getElementById('proveedor-fecha-inicio').value,
    fechaFin: document.getElementById('proveedor-fecha-fin').value,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/proveedores/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(proveedor),
    });

    if (response.ok) {
      alert('Proveedor modificado exitosamente.');
	  window.location.href = 'proveedores.html';
    } else {
		const errorText = await response.text();
		     alert(`Error: ${errorText}`);
	 }
  } catch (error) {
    console.error('Error al modificar proveedor:', error);
	alert('Error al guardar los cambios.');
  }
}