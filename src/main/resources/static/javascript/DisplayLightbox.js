const MessageTypes = window.ConvergeLightbox.MessageTypes;

const submitData = (data) => {
  const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
  const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

  // Convert object to URL-encoded string
  const urlEncodedData = new URLSearchParams(Object.entries(data)).toString();

  fetch('/epg/lightbox/epg-lb-3ds-results', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      [header]: token,
    },
    body: urlEncodedData
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.text();
  })
  .then(transaction => {
    document.querySelector('#purchasebtn').innerHTML = '<span>Purchase</span>';
    document.querySelector('#purchasebtn').classList.add('enabled');

    const trans = JSON.parse(transaction);
    alert("isAuthorized = " + trans.isAuthorized);
  })
  .catch(() => {
    alert("error");
  });
};

const handleError = (error) => {
  console.log(error);
};

let lightbox;

async function displayLightbox(sessionId)
{
  if (!lightbox) {
    lightbox = new window.ConvergeLightbox({
      sessionId: sessionId,
      onReady: (error) =>
        error
          ? console.error('Lightbox failed to load')
          : lightbox.show(),
      messageHandler: (message, defaultAction) => {
        switch (message.type) {
          case MessageTypes.transactionCreated:
          case MessageTypes.hostedCardCreated:
            submitData({
              sessionId: message.sessionId,
            });
            lightbox.destroy();
            lightbox = undefined;
            break;
          case MessageTypes.error:
            handleError({
              error: message.error,
            });
            break;
        }
        defaultAction();
      },
    });
  } else {
    lightbox.show();
  }
}
