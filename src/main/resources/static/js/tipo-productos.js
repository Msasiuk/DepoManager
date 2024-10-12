const API_BASE_URL = 'http://localhost:8080/api';

// Listar tipos de producto
async function fetchTipoProductos() {
  const response = await fetch(`${API_BASE_URL}/tipo-producto`);
  const tipos = await response.json();
  renderTipoProductos(tipos);
}

// Crear tipo de producto
document.getElementById('tipo-producto-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const codigo = document.getElementById('tipo-codigo').value;
  const descripcion = document.getElementById('tipo-descripcion').value;

  const response = await fetch(`${API_BASE_URL}/tipo-producto`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ codigo, descripcion })
  });

  if (response.ok) {
    alert('Tipo de producto creado exitosamente');
    fetchTipoProductos();
  } else {
    alert('Error: El código ya existe.');
  }
});

// Renderizar lista de tipos de producto
function renderTipoProductos(tipos) {
  const list = document.getElementById('tipo-producto-list');
  list.innerHTML = '';
  tipos.forEach(tipo => {
    const item = document.createElement('div');
    item.className = 'list-item';
    item.innerHTML = `
      <strong>${tipo.codigo}</strong>: ${tipo.descripcion}
      <button onclick="deleteTipoProducto(${tipo.id})">Eliminar</button>
    `;
    list.appendChild(item);
  });
}

// Eliminar tipo de producto
async function deleteTipoProducto(id) {
  if (confirm('¿Estás seguro de que deseas eliminar este tipo de producto?')) {
    const response = await fetch(`${API_BASE_URL}/tipo-producto/${id}`, { method: 'DELETE' });
    if (response.ok) {
      alert('Tipo de producto eliminado');
      fetchTipoProductos();
    }
  }
}

// Inicializar la carga de datos
fetchTipoProductos();