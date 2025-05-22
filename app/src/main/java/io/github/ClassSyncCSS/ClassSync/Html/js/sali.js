async function loadCSV(url) {
  const response = await fetch(url);
  const text = await response.text();
  return Papa.parse(text, { header: true }).data;
}

async function main() {
  const sali = await loadCSV('data/sali.csv');
  const lista = document.getElementById('listaSali');
  sali.forEach(s => {
    if (s.name && s.tip) {
      const li = document.createElement('li');
      const link = document.createElement('a');
      link.href = `orar_sali.html?sala=${encodeURIComponent(s.name)}`;
      link.textContent = `${s.name} - ${s.tip}`;
      li.appendChild(link);
      lista.appendChild(li);
    }
  });
}

window.main = main;
