

import EdufinderFullLogo from "/src/assets/images/edufinder/EdufinderFullLogo.png";
import { Link } from "react-router";
const FullLogo = () => {
  return (
    <Link to={"/"}>
      <img src={EdufinderFullLogo} alt="logo" className="block" />
    </Link>
  );
};

export default FullLogo;
