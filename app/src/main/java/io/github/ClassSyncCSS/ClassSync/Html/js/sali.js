async function loadCSV(url) {
  const response = await fetch(url);
  const text = await response.text();
  const parsed = Papa.parse(text, { header: true }).data;

  console.assert(Array.isArray(parsed), `Eroare: Fisierul ${url} nu a fost procesat corect ca array.`);
  return parsed;
}

async function main() {
  const sali = await loadCSV('data/sali.csv');
  console.assert(sali.length > 0, 'Eroare: Nicio sala incarcata din sali.csv.');

  const lista = document.getElementById('listaSali');
  console.assert(lista, 'Eroare: Elementul cu id="listaSali" nu a fost gasit.');

  sali.forEach(s => {
    console.assert('name' in s && 'tip' in s, `Eroare: Intrare invalida in sali.csv: ${JSON.stringify(s)}`);

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
