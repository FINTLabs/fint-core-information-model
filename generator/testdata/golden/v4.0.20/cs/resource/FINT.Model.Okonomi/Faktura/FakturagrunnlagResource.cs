using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{

    public class FakturagrunnlagResource 
    {

    
        public long? Avgiftsbelop { get; set; }
        public List<FakturalinjeResource> Fakturalinjer { get; set; }
        public DateTime? Leveringsdato { get; set; }
        public FakturamottakerResource Mottaker { get; set; }
        public long? Nettobelop { get; set; }
        public Identifikator Ordrenummer { get; set; }
        public long? Totalbelop { get; set; }
        
        public FakturagrunnlagResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddFaktura(Link link)
        {
            AddLink("faktura", link);
        }

        public void AddFakturautsteder(Link link)
        {
            AddLink("fakturautsteder", link);
        }
    }
}
