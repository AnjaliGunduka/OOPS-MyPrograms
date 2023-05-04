<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Activate Account</title>

    <style type="text/css">
        @import url('https://fonts.googleapis.com/css2?family=Poppins&display=swap');
        @font-face {
            font-family: 'Poppins';
            font-style: normal;
            font-weight: 1000;
            font-display: swap;
            src: url(https://fonts.gstatic.com/s/poppins/v15/pxiByp8kv8JHgFVrLBT5Z1xlFd2JQEk.woff2) format('woff2');
            unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
        }

        html, body {
            text-content: center;
            max-width: 100%;
            min-height: 700px;
            background-color: white;
        }

        p {
            font-family: 'Nunito', Roboto, Arial;
            font-style: normal;
            font-weight: 100;
            font-variant-ligatures: normal;
            font-size: 12pt;
            letter-spacing: 1px;
            color: #848484;
        }

        h1 {
            font-family: "Nunito-Semibold, Poppins, Roboto, Arial";
            font-style: normal;
            font-weight: 100;
            font-variant-ligatures: normal;
            font-size: 2rem;
            letter-spacing: 1px;
        }

        button {
            border: 3px solid #CF795E;
            border-radius: 34px;
            opacity: 1;
            font-family: "Nunito-Semibold";
            font-style: normal;
            font-weight: 100;
            font-variant-ligatures: normal;
            font-size: 1rem;
            letter-spacing: .5px;
            padding-left: 20px;
            padding-right: 20px;
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .footerText {
            font-family: 'Poppins', Roboto, Arial;
            font-style: normal;
            font-weight: 300;
            font-variant-ligatures: normal;
            font-size: .55rem;
            letter-spacing: 1px;
            color: #848484;
        }

        .heightFiller {
            height: 35px;
        }

        @media only screen and (max-width: 360px) {
            .centerPanel {
                display: block;
                margin-right: auto;
                margin-left: auto;
                max-width: 360px;
                max-height: 100%;
                text-align: center;
            }
            .headerBar {
                height: 77px;
                width: 360px;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                /*background-image: url('${imageURL}curves.png');*/
                text-align: center;
                vertical-align: bottom;
            }
            .happyLogo {
                position: absolute;
                top: 0;
                left: 0;
                bottom: 18px;
                right: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto auto 0; /* position to bottom and center */
                padding: 10px;
            }

            .footerBar {
                height: 89px;
                width: 361px;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                bottom: 0px;
                /*background-image: url('curves_footer.png');*/
                vertical-align: bottom;
            }
            .footerText {
                position: absolute;
                align-content: center;
                font-size: 9pt;
                bottom: 10px;
                right: 0;
                left: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto;
                padding: 15px;
            }

            .footerBlock {
                margin: 10px;
            }

            .Main {
                padding-top: 105px;
                padding-bottom: 105px;
                margin-left: 35px;
                margin-right: 35px;
            }
        }

        @media only screen and (min-width: 361px) {
            .centerPanel {
                display: block;
                margin-right: auto;
                margin-left: auto;
                max-width: 563px;
                max-height: 100%;
                text-align: center;
            }
            .headerBar {
                height: 77px;
                width: 100%;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                /*background-image: url('${imageURL}curves.png');*/
                text-align: center;
                vertical-align: bottom;
            }
            .happyLogo {
                position: absolute;
                top: 0;
                left: 0;
                bottom: 18px;
                right: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto auto 0; /* position to bottom and center */
                padding: 15px;
            }

            .footerBar {
                height: 89px;
                width: 100%;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                bottom: 0px;
                /*background-image: url('curves_footer.png');*/
                vertical-align: bottom;
            }
            .footerText {
                position: absolute;
                align-content: center;
                font-size: 9pt;
                bottom: 10px;
                right: 0;
                left: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto;
                padding: 15px;
            }
            .footerBlock {
                margin: 5px;
            }

            .Main {
                padding-top: 105px;
                padding-bottom: 105px;
                margin-left: 35px;
                margin-right: 35px;
            }
        }

        @media only screen and (min-width: 769px) {
            .centerPanel {
                display: block;
                margin-right: auto;
                margin-left: auto;
                /*max-width: 563px;*/
                min-height: 800px;
                text-align: center;
                margin-bottom: 0;
            }

            .headerBar {
                height: 77px;
                width: 100%;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                /*background-image: url('${imageURL}curves.png');*/
                text-align: center;
                vertical-align: bottom;

            }

            .happyLogo {
                position: absolute;
                top: 0;
                left: 0;
                bottom: 18px;
                right: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto auto 0; /* position to bottom and center */
                padding: 15px;
            }

            .footerBar {
                height: 89px;
                width: 100%;
                background-repeat: no-repeat;
                background-position: center;
                position: relative;
                bottom: 0px;
                /*background-image: url('curves_footer.png');*/
                vertical-align: bottom;
                padding: 10px;
            }

            .footerText {
                position: absolute;
                align-content: center;
                font-size: 9pt;
                bottom: 10px;
                right: 0;
                left: 0;
                width: auto; /* to keep proportions */
                height: auto; /* to keep proportions */
                max-width: 100%; /* not to stand out from div */
                max-height: 100%; /* not to stand out from div */
                margin: auto;
                padding: 10px;
            }
            .footerBlock {
                margin: 5px;
            }

            .Main {
                padding-top: 105px;
                padding-bottom: 105px;
                margin-left: 35px;
                margin-right: 35px;
            }
        }

        a.button {
            -webkit-appearance: button;
            -moz-appearance: button;
            appearance: inherit;

            text-decoration: none;
            color: initial;
        }
    </style>
</head>

<body>
<div class="centerPanel">
    <!-- header -->
    <div class="heightFiller"></div>
    <div class="headerBar">
        <img class="happyLogo" src="${imageURL}juno-logo-11.png"/>
    </div>
    <!-- body -->
    <div class="Main">
        <div><p>Please click the link below to activate your Juno Early Gender Test kit and set your account password with us.</p></div>
        <div>&nbsp;</div>
        <div><a href="${locationURL}" target="_parent"><button>Activate Kit and Account</button></a></div>
    </div>
    <!-- footer -->
    <div class="footerBar">
        <div class="footerText">
            <div class="footerBlock">&copy; Juno DX</div>
            <div class="footerBlock">11535 Sorrento Valley Rd, Suite 407, San Diego, CA 92121</div>
            <div >You are receiving this update because you signed up for updates from Juno Diagnostics.</div>
        </div>
    </div>
</div>
</body>
</html>

