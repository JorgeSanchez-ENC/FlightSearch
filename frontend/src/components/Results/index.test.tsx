import { render, screen } from '@testing-library/react';
import Results from '.';



test('renders the flight results with sort buttons', () => {
  render(<Results/>);

  expect(screen.getByText(/Results/i)).toBeInTheDocument();
  expect(screen.getByText(/Sort by:/i)).toBeInTheDocument();
  expect(screen.getByText(/price/i)).toBeInTheDocument();
});