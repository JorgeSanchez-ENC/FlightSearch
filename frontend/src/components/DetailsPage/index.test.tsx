
import { render, screen } from '@testing-library/react';
import DetailsPage from '.';


test('renders flight details and traveler fare details', () => {
  render(<DetailsPage/>);

  expect(screen.getByText(/Flight Details/i)).toBeInTheDocument();

});