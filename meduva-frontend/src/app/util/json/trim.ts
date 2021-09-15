export function trimJSON(json: any, properties: string[]) {
  properties.forEach(propName => {
    delete json[propName];
  });
  return json;
}
