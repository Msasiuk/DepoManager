const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
	fetchDepositos();
	fetchMovimientos();
	fetchProveedores();
	inicializarFormulario();
	
	// Lógica para mostrar/ocultar campos según el tipo de movimiento
	document.getElementById('tipo-movimiento').addEventListener('change', gestionarCamposMovimiento);
	document.getElementById('agregar-producto-btn').addEventListener('click', agregarDetalleProducto);
});

function gestionarCamposMovimiento() {
	const tipoMovimiento = document.getElementById('tipo-movimiento').value;
	const proveedorSelect = document.getElementById('proveedor-id');
	const depositoOrigenSelect = document.getElementById('deposito-origen-id');
	const depositoDestinoSelect = document.getElementById('deposito-destino-id');
	
	if (tipoMovimiento === 'INGRESO') {
		proveedorSelect.style.display = 'block';
		depositoDestinoSelect.style.display = 'block';
		depositoOrigenSelect.style.display = 'none';
	} else if (tipoMovimiento === 'EGRESO') {
		proveedorSelect.style.display = 'none';
		depositoOrigenSelect.style.display = 'block';
		depositoDestinoSelect.style.display = 'none';
	} else if (tipoMovimiento === 'ROTACION') {
		proveedorSelect.style.display = 'none';
		depositoOrigenSelect.style.display = 'block';
		depositoDestinoSelect.style.display = 'block';
	}
}

async function registrarMovimiento(movimiento) {
	try {
		const response = await fetch(`${API_BASE_URL}/movimientos`, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(movimiento)
		});
		
		if (!response.ok) {
			const errorData = await response.json();
			alert(`Error: ${errorData.message || 'No se pudo registrar el movimiento.'}`);
			return;
		}
		
		alert('Movimiento registrado exitosamente.');
	} catch (error) {
		console.error('Error al registrar movimiento:', error);
		alert('Error de conexión.');
	}
}
  
function inicializarFormulario() {
	const movimientoForm = document.getElementById('movimiento-form');
	movimientoForm.addEventListener('submit', async (event) => {
		event.preventDefault();
		
		const tipoMovimiento = document.getElementById('tipo-movimiento').value;
		const proveedorId = document.getElementById('proveedor-id').value || null;
		const depositoOrigenId = document.getElementById('deposito-origen-id').value || null;
		const depositoDestinoId = document.getElementById('deposito-destino-id').value || null;
		const nroComprobante = document.getElementById('nro-comprobante').value;
		const fechaMovimiento = new Date().toISOString().split('T')[0];
		const detalles = Array.from(document.querySelectorAll('.detalle-producto')).map(detalle => ({
			producto: { id: parseInt(detalle.querySelector('.producto-id').value) },
			cantidad: parseInt(detalle.querySelector('.cantidad').value),
			fechaVencimiento: detalle.querySelector('.fecha-vencimiento').value || null
		}));
		
		const movimiento = {
			tipoMovimiento,
			proveedor: proveedorId ? { id: parseInt(proveedorId) } : null,
			depositoOrigen: depositoOrigenId ? { id: parseInt(depositoOrigenId) } : null,
			depositoDestino: depositoDestinoId ? { id: parseInt(depositoDestinoId) } : null,
			nroComprobante,
			fechaMovimiento,
			detalles
		};
		
		await registrarMovimiento(movimiento);
		movimientoForm.reset();
		fetchMovimientos();
	});
}

function agregarDetalleProducto() {
	const detalleList = document.getElementById('detalle-producto-list');
	const detalleDiv = document.createElement('div');
	detalleDiv.className = 'detalle-producto';
	
	detalleDiv.innerHTML = `<select class="producto-id" required>
			<option value="" disabled selected>Seleccione un Producto</option>
		</select>
		<input type="number" class="cantidad" placeholder="Cantidad" required>
		<input type="date" class="fecha-vencimiento">
		<button type="button" class="eliminar-detalle-btn">Eliminar</button>`;
		
	detalleDiv.querySelector('.eliminar-detalle-btn').addEventListener('click', () => {
		detalleDiv.remove();
	});
	
	detalleList.appendChild(detalleDiv);
	fetchProductos(detalleDiv.querySelector('.producto-id'));
}

async function fetchMovimientos() {
	try {
		const response = await fetch(`${API_BASE_URL}/movimientos`);
		if (!response.ok) {
			throw new Error(`Error: ${response.status}`);
		}
		
		const movimientos = await response.json() || [];
		console.log('Movimientos recibidos:', movimientos); // Agregar log para depurar
		renderMovimientos(movimientos);
	} catch (error) {
		console.error('Error al cargar movimientos:', error);
	}
}

function renderMovimientos(movimientos) {
	const list = document.getElementById('movimiento-list');
	list.innerHTML = ''; // Limpiar lista anterior
	
	if (movimientos.length === 0) {
		list.innerHTML = '<p>No hay movimientos registrados.</p>';
		return;
	}
	
	movimientos.forEach(movimiento => {
		const depositoOrigen = movimiento.depositoOrigen?.descripcion || 'Sin Origen';
		const depositoDestino = movimiento.depositoDestino?.descripcion || 'Sin Destino';
		const item = document.createElement('div');
		item.className = 'list-item';
		
		item.innerHTML = `<div>
			<strong>Tipo:</strong> ${movimiento.tipoMovimiento} <br>
			<strong>Fecha:</strong> ${movimiento.fechaMovimiento} <br>
			<strong>Comprobante:</strong> ${movimiento.nroComprobante || 'N/A'} <br>
			<strong>Depósito Origen:</strong> ${depositoOrigen} <br>
			<strong>Depósito Destino:</strong> ${depositoDestino}
		</div>`;
		list.appendChild(item);
	});
}

async function fetchProductos(selectElement) {
	try {
		const response = await fetch(`${API_BASE_URL}/productos`);
		const productos = await response.json();
		renderSelectOptions(selectElement, productos);
	} catch (error) {
		console.error('Error al cargar productos:', error);
	}
}

async function fetchDepositos() {
	try {
		const response = await fetch(`${API_BASE_URL}/depositos`);
		const depositos = await response.json();
		renderSelectOptions(document.getElementById('deposito-origen-id'), depositos);
		renderSelectOptions(document.getElementById('deposito-destino-id'), depositos);
	} catch (error) {
		console.error('Error al cargar depósitos:', error);
	}
}

function renderSelectOptions(selectElement, items) {
	selectElement.innerHTML = '';
	
	items.forEach(item => {
		const option = document.createElement('option');
		option.value = item.id;
		option.textContent = item.nombre || item.descripcion;
		selectElement.appendChild(option);
	});
}

async function fetchProveedores() {
	try {
		const response = await fetch(`${API_BASE_URL}/proveedores`);
		if (!response.ok) {
			throw new Error(`Error al cargar proveedores: ${response.status}`);
		}
		const proveedores = await response.json();
		console.log('Proveedores recibidos:', proveedores); // Para depurar
		renderSelectOptions(document.getElementById('proveedor-id'), proveedores);
	} catch (error) {
		console.error('Error al cargar proveedores:', error);
	}
}