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
		if (!response.ok) throw new Error('Proveedor no encontrado');
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
	// Verifica si la fecha es válida antes de formatearla
	if (!fecha) return null;
	const date = new Date(fecha);
	if (isNaN(date)) return null; // Manejo de fechas inválidas
	return date.toISOString().split('T')[0]; // Formato 'YYYY-MM-DD'
}

async function guardarCambios() {
	const fechaInicio = document.getElementById('proveedor-fecha-inicio').value;
	const fechaFin = document.getElementById('proveedor-fecha-fin').value;
	
	// Validación en el frontend: Fecha fin no puede ser menor que fecha inicio
	if (new Date(fechaFin) < new Date(fechaInicio)) {
		alert('La fecha de finalización no puede ser anterior a la fecha de inicio.');
		return; // Detener el envío
	}
	
	const proveedor = {
		id: proveedorId,  // Asignar correctamente el ID desde la variable global
		cuitCuil: document.getElementById('proveedor-cuit-cuil').value,
		nombre: document.getElementById('proveedor-nombre').value,
		razonSocial: document.getElementById('proveedor-razon-social').value,
		fechaInicio: formatFecha(fechaInicio),
		fechaFin: formatFecha(fechaFin),
	};
	
	try {
		const response = await fetch(`${API_BASE_URL}/proveedores/${proveedor.id}`, {
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