const { TextEncoder, TextDecoder } = require('util');
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('index.html', () => {
  let dom;
  let document;

  beforeAll(() => {
    const html = fs.readFileSync(path.resolve(__dirname, '../index.html'), 'utf8');
    dom = new JSDOM(html);
    document = dom.window.document;
  });

  test('titlul paginii este corect', () => {
    expect(document.title).toBe('Orar Universitate');
  });

  test('există un element <header>', () => {
    const header = document.querySelector('header');
    expect(header).not.toBeNull();
  });

  test('există un link către studenti.html', () => {
    const link = Array.from(document.querySelectorAll('a')).find(a => a.href.includes('studenti.html'));
    expect(link).toBeDefined();
  });

  test('există un link către profesori.html', () => {
    const link = Array.from(document.querySelectorAll('a')).find(a => a.href.includes('profesori.html'));
    expect(link).toBeDefined();
  });

  test('există un link către sali.html', () => {
    const link = Array.from(document.querySelectorAll('a')).find(a => a.href.includes('sali.html'));
    expect(link).toBeDefined();
  });

  test('există un link către discipline.html', () => {
    const link = Array.from(document.querySelectorAll('a')).find(a => a.href.includes('discipline.html'));
    expect(link).toBeDefined();
  });
});
