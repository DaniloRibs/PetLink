export function speciesIcon(species: string): string {
    switch (species) {
        case 'cachorro': return '🐶';
        case 'gato': return '🐱';
        case 'passaro': return '🐦';
        default: return '🐾';
    }
}