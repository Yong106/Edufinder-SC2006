
import EdufinderLogo from '/src/assets/images/edufinder/EdufinderLogo.png'
import { Link } from 'react-router';

const Logo = () => {
  return (
   <Link to={'/'}>
      <img src={EdufinderLogo} alt="logo" />
    </Link>
  )
}

export default Logo
