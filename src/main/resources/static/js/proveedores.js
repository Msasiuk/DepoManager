const API_BASE_URL = 'http://localhost:8080/api';
let proveedorIdActual = null;

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
  fetchProveedores();
  inicializarFormulario();
});

function inicializarFormulario() {
  const proveedorForm = document.getElementById('proveedor-form');
  
  proveedorForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const nuevoProveedor = {
      cuitCuil: document.getElementById('proveedor-cuit-cuil').value,
      nombre: document.getElementById('proveedor-nombre').value,
      razonSocial: document.getElementById('proveedor-razon-social').value,
	  fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
	  fechaFin: document.getElementById('proveedor-fecha-fin').value || '9999-12-31',
    };

    await crearProveedor(nuevoProveedor);
    proveedorForm.reset();
    fetchProveedores();
  });
}

async function fetchProveedores() {
  try {
    const response = await fetch(`${API_BASE_URL}/proveedores`);
    const proveedores = await response.json();
    renderProveedores(proveedores);
  } catch (error) {
    console.error('Error al cargar proveedores:', error);
  }
}

function renderProveedores(proveedores) {
  const list = document.getElementById('proveedor-list');
  list.innerHTML = '';

  proveedores.forEach(proveedor => {
    const item = document.createElement('div');
    item.className = 'list-item';
    item.innerHTML = `
      <div>
        <strong>${proveedor.cuitCuil}</strong> - ${proveedor.nombre} <br>
        <small>Razón Social: ${proveedor.razonSocial || 'N/A'}</small> <br>
        <small>Fecha Inicio: ${formatFecha(proveedor.fechaInicio)} | Fecha Fin: ${formatFecha(proveedor.fechaFin)}</small>
      </div>
      <button onclick="deleteProveedor(${proveedor.id})">Eliminar</button>
      <a href="modificar-proveedor.html?id=${proveedor.id}"><button>Modificar</button></a>
    `;
    list.appendChild(item);
  });
}

function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

async function crearProveedor(proveedor) {
  try {
    const response = await fetch(`${API_BASE_URL}/proveedores`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(proveedor),
    });

    if (response.ok) {
      alert('Proveedor creado exitosamente.');
    } else {
      alert('Error: El CUIT/CUIL ya existe.');
    }
  } catch (error) {
    console.error('Error al crear proveedor:', error);
  }
}

async function deleteProveedor(id) {
  if (confirm('¿Está seguro de que desea eliminar este proveedor?')) {
    try {
      const response = await fetch(`${API_BASE_URL}/proveedores/${id}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Proveedor eliminado exitosamente.');
        fetchProveedores();
      }
    } catch (error) {
      console.error('Error al eliminar proveedor:', error);
    }
  }
}