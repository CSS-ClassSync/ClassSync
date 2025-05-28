global.TextEncoder = require('util').TextEncoder;
global.TextDecoder = require('util').TextDecoder;

const { JSDOM } = require('jsdom');
const fs = require('fs');
const path = require('path');

describe('discipline.js', () => {
  let dom;
  let container;

  beforeEach(() => {
    const html = fs.readFileSync(path.resolve(__dirname, '../discipline.html'), 'utf8');
    dom = new JSDOM(html, { runScripts: 'dangerously', resources: 'usable' });
    container = dom.window.document.body;

    // Mock pentru fetch
    global.fetch = jest.fn((url) => {
      if (url.includes('materii.csv')) {
        return Promise.resolve({ text: () => Promise.resolve(`id,name,tip\n1,POO,Curs\n2,BD,Curs`) });
      }
      if (url.includes('profesori_materii.csv')) {
        return Promise.resolve({ text: () => Promise.resolve(`materie,profesor\n1,1\n1,2`) });
      }
      if (url.includes('profesori.csv')) {
        return Promise.resolve({ text: () => Promise.resolve(`id,name\n1,Prof. Ionescu\n2,Prof. Popescu`) });
      }
      return Promise.reject(new Error(`Unknown URL: ${url}`));
    });
  });

  test('afiseaza lista de discipline', async () => {
    await new Promise((resolve) => {
      dom.window.addEventListener('load', resolve);
    });

    const lista = container.querySelector('#listaDiscipline');
    expect(lista).not.toBeNull();
    expect(lista.children.length).toBe(2); 
  });
  
});
