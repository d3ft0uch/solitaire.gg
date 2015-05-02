define(function () {
  "use strict";

  var margin = 0.7;
  var padding = 0.2;

  function getDimensions(pileSet) {
    if(pileSet.dimensions !== undefined) {
      return pileSet.dimensions;
    }
    var ret = [pileSet.piles.length * (1 + padding), 1 + padding];
    switch(pileSet.behavior) {
      case "waste":
        var wasteCardsShown = pileSet.piles[0].options.cardsShown;
        if(wasteCardsShown === 3) {
          ret = [2 * (1 + padding), ret[1]];
        } else {
          ret = [1 + (wasteCardsShown * padding * 1.5), ret[1]];
        }
        break;
      case "tableau":
        var maxCards = _.max(pileSet.piles, function(pile) { return pile.cards.length; }).cards.length;
        ret = [ret[0], ret[1] + ((maxCards - 2) * padding)];
        break;
      case "pyramid":
        var rows = 1;
        var rowCounter = 0;
        _.each(pileSet.piles, function() {
          if(rowCounter === rows) {
            rows += 1;
            rowCounter -= rowCounter;
          }

          rowCounter += 1;
        });
        pileSet.rows = rows;
        ret = [rows * (1 + padding), (rows * 0.5) + 0.5 + padding];
        break;
      default:
    }
    pileSet.dimensions = ret;
    return ret;
  }


  function calculateLayout(pileSets, layout, aspectRatio) {
    console.log("Creating layout for [" + pileSets.length + "] pile sets using layout [" + layout + "] with aspect ratio of [" + aspectRatio + "].");
    var locations = {};
    var xOffset = margin;
    var yOffset = margin;
    var remainingPileSets = pileSets;
    var currentRowMaxHeight = 1.0;
    var maxWidth = 0;
    var lastChar = null;

    function newRow() {
      if(xOffset > maxWidth) {
        maxWidth = xOffset;
      }
      xOffset = margin;
      yOffset += currentRowMaxHeight;
      currentRowMaxHeight = 1;
    }

    function processCharacter(c) {
      var pileSet;
      switch(c) {
        case 's': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "stock"; }); break;
        case 'w': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "waste"; }); break;
        case 'f': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "foundation"; }); break;
        case 't': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "tableau"; }); break;
        case 'c': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "cell"; }); break;
        case 'r': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "reserve"; }); break;
        case 'p': pileSet = _.find(remainingPileSets, function(ps) { return ps.behavior === "pyramid"; }); break;
        default:
      }
      switch(c) {
        case ':':
          xOffset += 1 + padding;
          break;
        case '.':
          xOffset += (1 + padding) / 2;
          break;
        case '|':
          newRow();
          break;
        default:
          if(pileSet === undefined) {
            throw "Unable to find set matching [" + c + "]";
          }
          remainingPileSets = _.without(remainingPileSets, pileSet);
          var pileSetDimensions = getDimensions(pileSet);
          //console.log(pileSet);
          if(pileSet.visible !== undefined && !pileSet.visible) { // Hide this pile
            _.each(pileSet.piles, function(pile, pileIndex) {
              //console.log("Adding [" + pile.id + "]!");
              locations[pile.id] = {x: (pileIndex * (1 + padding)) + 0.5, y: -0.5};
            });
          } else {
            if(pileSet.behavior === "pyramid") {
              var currentRow = 1;
              var rowCounter = 0;
              xOffset = margin + ((pileSet.rows - currentRow) / 2) * (1 + padding);
              _.each(pileSet.piles, function(pile) {
                if(rowCounter === currentRow) {
                  currentRow += 1;
                  rowCounter -= rowCounter;
                  xOffset = margin + ((pileSet.rows - currentRow) / 2) * (1 + padding);
                }

                locations[pile.id] = {x: xOffset, y: yOffset + ((currentRow - 1) * 0.5)};
                xOffset = xOffset + 1 + padding;
                rowCounter += 1;
              });
            } else {
              var originalXOffset = xOffset;
              _.each(pileSet.piles, function(pile) {
                locations[pile.id] = {x: xOffset, y: yOffset};
                xOffset = xOffset + 1 + padding;
              });
              xOffset = originalXOffset + pileSetDimensions[0];
            }
            if(pileSetDimensions[1] > currentRowMaxHeight) {
              currentRowMaxHeight = pileSetDimensions[1];
            }
          }
          break;
      }
      lastChar = c;
    }

    _.each(layout.split(''), function(char) {
      processCharacter(char);
    });
    newRow();

    return { "width": maxWidth - (margin / 2), "height": yOffset - 0.5, "locations": locations };
  }

  return calculateLayout;
});
