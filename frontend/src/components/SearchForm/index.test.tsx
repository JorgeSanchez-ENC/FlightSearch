import { render, screen, fireEvent } from '@testing-library/react';
import SearchForm from '.';

test('renders the search form', () => {
  render(<SearchForm />);

  expect(screen.getByLabelText(/departure airport/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/destination airport/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/departure date/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/adults/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/currency/i)).toBeInTheDocument();

});