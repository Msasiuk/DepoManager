const API_BASE_URL = 'http://localhost:8080/api';

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
	fetchProveedores();
	inicializarFormulario();
	
	// Inicializa el evento para filtrar productos
	const buscarCodigoInput = document.getElementById('buscar-codigo');
	buscarCodigoInput.addEventListener('input', filtrarProveedores);
});

// Inicializar evento del formulario
function inicializarFormulario() {
	const proveedorForm = document.getElementById('proveedor-form');
	
	// Registrar el evento 'submit' del formulario
	proveedorForm.addEventListener('submit', async (event) => {
		event.preventDefault();
		
		const nuevoProveedor = {
			cuitCuil: document.getElementById('proveedor-cuit-cuil').value,
			nombre: document.getElementById('proveedor-nombre').value,
			razonSocial: document.getElementById('proveedor-razon-social').value,
			fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
			fechaFin: document.getElementById('proveedor-fecha-fin').value || '9999-12-31',
		};
		
		console.log(JSON.stringify(nuevoProveedor)); // Verifica la salida en consola
		
		// Llamada al backend
		await crearProveedor(nuevoProveedor);
		
		// Resetear formulario y actualizar la lista
		proveedorForm.reset();
		fetchProveedores();
	});
}

async function crearProveedor(proveedor) {
	try {
		const response = await fetch(`${API_BASE_URL}/proveedores`, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(proveedor),
		});
		
		if (!response.ok) {
			const errorData = await response.json();
			if (response.status === 409) {
				alert(`Error: ${errorData.message}`);
			} else {
				alert(`Error inesperado: ${errorData.message || 'No se pudo crear el proveedor.'}`);
			}
			return;
		}
		alert('Proveedor creado exitosamente.');
	} catch (error) {
		console.error('Error al crear el proveedor:', error);
		alert('Error de conexión. No se pudo crear el tipo producto.');
	}
}

// Obtener y mostrar los proveedores
async function fetchProveedores() {
	try {
		const response = await fetch(`${API_BASE_URL}/proveedores`);
		if (!response.ok) {
			throw new Error(`Error: ${response.status}`);
		}
		const proveedores = await response.json();
		
		// Limpiar el campo de búsqueda al recargar la lista
		document.getElementById('buscar-codigo').value = '';
		renderProveedores(Array.isArray(proveedores) ? proveedores : []);
	} catch (error) {
		console.error('Error al cargar tipos de proveedores:', error);
	}
}

// Renderizar tipos de producto en la pantalla
function renderProveedores(proveedores) {
	const list = document.getElementById('proveedor-list');
	list.innerHTML = '';
	
	proveedores.forEach(proveedor => {
		const item = document.createElement('div');
		item.className = 'list-item';
		item.innerHTML = `<div>
			<strong>${proveedor.cuitCuil}</strong> - ${proveedor.nombre} <br>
			<small>Razón Social: ${proveedor.razonSocial || 'N/A'}</small> <br>
			<small>Fecha Inicio: ${formatFecha(proveedor.fechaInicio)} | Fecha Fin: ${formatFecha(proveedor.fechaFin)}</small>
			</div>
			<button onclick="deleteProveedor(${proveedor.id})">Eliminar</button>
			<a href="modificar-proveedor.html?id=${proveedor.id}"><button>Modificar</button></a>`;
			list.appendChild(item);
		});
}

function formatFecha(fecha) {
	return new Date(fecha).toISOString().split('T')[0];
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

function filtrarProveedores() {
	const terminoBusqueda = document.getElementById('buscar-codigo').value.toLowerCase();
	const proveedores = document.querySelectorAll('#tipo-producto-list .list-item');
	
	proveedores.forEach((proveedor) => {
		const codigoProveedor = proveedor.querySelector('strong').textContent.toLowerCase();
		if (codigoProveedor.includes(terminoBusqueda)) {
			proveedor.style.display = ''; // Mostrar si coincide
		} else {
			proveedor.style.display = 'none'; // Ocultar si no coincide
		}
	});
}